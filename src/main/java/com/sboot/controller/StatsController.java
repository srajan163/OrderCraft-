package com.sboot.controller;

import com.sboot.service.StatsService;
import com.sboot.service.StatsService.StatsOverview;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:4200") // adjust origin for prod
public class StatsController {

    private final StatsService statsService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    // one-shot endpoint
    @GetMapping("/overview")
    public StatsOverview overview() {
        return statsService.getOverview();
    }

    // SSE stream: sends "stats" event every 5 seconds
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamStats() {
        final SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 min timeout

        executor.submit(() -> {
            try {
                while (true) {
                    StatsOverview overview = statsService.getOverview();
                    emitter.send(SseEmitter.event().name("stats").data(overview));
                    Thread.sleep(Duration.ofSeconds(5).toMillis()); // frequency
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (InterruptedException e) {
                emitter.complete();
                Thread.currentThread().interrupt();
            }
        });

        emitter.onCompletion(() -> { /* optional cleanup */ });
        emitter.onTimeout(emitter::complete);

        return emitter;
    }
}
	