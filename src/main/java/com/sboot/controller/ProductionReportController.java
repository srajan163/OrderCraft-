package com.sboot.controller;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sboot.dto.ProductionReportRequest;
import com.sboot.dto.ProductionReportResponse;
import com.sboot.service.ProductionReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports/production")
public class ProductionReportController {
 
    private final ProductionReportService reportService;
 //changes done and code updated
    public ProductionReportController(ProductionReportService reportService) {
        this.reportService = reportService;
    }
 
    @PostMapping
    public ResponseEntity<List<ProductionReportResponse>> generateReport(
            @RequestBody ProductionReportRequest request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }
    
    @GetMapping("/export")
    public ResponseEntity<?> exportReport(@RequestParam(defaultValue = "json") String format) throws Exception {
        List<ProductionReportResponse> reports = reportService.generateReport(new ProductionReportRequest());

        switch (format.toLowerCase()) {
            case "excel":
                byte[] excelBytes = reportService.exportToExcel(reports);
                return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=production_report.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelBytes);

            case "pdf":
                byte[] pdfBytes = reportService.exportToPdf(reports);
                return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=production_report.pdf")
                    .header("Content-Type", "application/pdf")
                    .body(pdfBytes);

            default:
                return ResponseEntity.ok(reports);
        }
    }

    

 
}
 
 