package com.sboot.controller;

import com.sboot.service.ProductionEfficiencyReportService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productionefficiencyreports")
@CrossOrigin(origins = "*")
public class ProductionEfficiencyReportController {

    private final ProductionEfficiencyReportService reportService;

    public ProductionEfficiencyReportController(ProductionEfficiencyReportService reportService) {
        this.reportService = reportService;
    }

    // 🔹 Generate report for one unit or all
    @GetMapping("/efficiency")
    public Map<String, Object> generateReport(
            @RequestParam(required = false) Long unitId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return reportService.generateEfficiencyReport(unitId, startDate, endDate);
    }

    // 🔹 Generate report for all production units
    @GetMapping("/efficiency/all")
    public List<Map<String, Object>> generateAllUnitsReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return reportService.generateAllUnitsReport(startDate, endDate);
    }
}
