package com.sboot.controller;

import com.sboot.service.TrendAnalysisReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//Trend Controller

@RestController
@RequestMapping("/api/reports/trends")
@CrossOrigin(origins = "*")
public class TrendAnalysisReportController {

    private final TrendAnalysisReportService reportService;

    public TrendAnalysisReportController(TrendAnalysisReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> generateTrendReport(
            @RequestParam String metric,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        Map<String, Object> result = reportService.generateTrendReport(metric, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
