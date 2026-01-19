package com.sboot.controller;

import com.sboot.service.ProductionAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/analytics")

@PreAuthorize("hasRole('PRODUCTION_MANAGER')")

//Analytics classssss
public class AnalyticsController {

    @Autowired
    private ProductionAnalyticsService analyticsService;

    @GetMapping("/production-summary")
    public Map<String, Object> getProductionSummary() {
        return analyticsService.getProductionSummary();
    }
}
