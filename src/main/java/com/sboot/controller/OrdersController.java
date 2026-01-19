package com.sboot.controller;

import com.sboot.service.PurchaseOrderService;
import com.sboot.dto.OrderSummaryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdersController {

    private final PurchaseOrderService orderService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public OrdersController(PurchaseOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamOrders() {
        final SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    OrderSummaryDto summary = orderService.getLiveOrderSummary();
                    emitter.send(SseEmitter.event().name("orders").data(summary));
                    Thread.sleep(Duration.ofSeconds(5).toMillis());
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                emitter.complete();
                Thread.currentThread().interrupt();
            }
        });

        emitter.onCompletion(() -> {});
        emitter.onTimeout(emitter::complete);
        return emitter;
    }
}
