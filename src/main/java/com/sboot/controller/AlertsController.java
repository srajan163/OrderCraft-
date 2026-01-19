package com.sboot.controller;

import com.sboot.service.ProductStockViewService;
import com.sboot.dto.AlertDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:4200")
public class AlertsController {

    private final ProductStockViewService stockService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public AlertsController(ProductStockViewService stockService) {
        this.stockService = stockService;
    }
//alerts
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAlerts() {
        final SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    List<AlertDto> alerts = stockService.getLowStockAlerts();
                    // send array (client will parse)
                    emitter.send(SseEmitter.event().name("alerts").data(alerts));
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
