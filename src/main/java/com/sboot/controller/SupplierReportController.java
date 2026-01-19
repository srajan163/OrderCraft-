package com.sboot.controller;

import com.sboot.dto.SupplierPerformanceReportDTO;
import com.sboot.service.SupplierPerformanceService;
import com.sboot.util.ExcelReportGenerator;
import com.sboot.util.PdfReportGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/suppliers")
public class SupplierReportController {

    private final SupplierPerformanceService performanceService;
    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    public SupplierReportController(SupplierPerformanceService performanceService,
                                    PdfReportGenerator pdfReportGenerator,
                                    ExcelReportGenerator excelReportGenerator) {
        this.performanceService = performanceService;
        this.pdfReportGenerator = pdfReportGenerator;
        this.excelReportGenerator = excelReportGenerator;
    }

    // ================================================================================
    // NEW: JSON LIST → All suppliers performance (useful for frontend preview/debugging)
    // ================================================================================
    @GetMapping
    public List<SupplierPerformanceReportDTO> getAllSuppliersPerformance() {
        return performanceService.getAllSupplierPerformances();
    }

    // ================================================================================
    // ALL SUPPLIERS → PDF
    // ================================================================================
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getAllSuppliersPdf() {

        byte[] pdf = performanceService.generateAllSuppliersPDF();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_suppliers_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ================================================================================
    // ALL SUPPLIERS → EXCEL
    // ================================================================================
    @GetMapping("/excel")
    public ResponseEntity<byte[]> getAllSuppliersExcel() {

        byte[] excel = performanceService.generateAllSuppliersExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_suppliers_report.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    // ================================================================================
    // SINGLE SUPPLIER → JSON Report
    // ================================================================================
    @GetMapping("/{id}/performance")
    public SupplierPerformanceReportDTO getPerformance(@PathVariable Long id) {
        return performanceService.getReportForSupplier(id);
    }

    // ================================================================================
    // SINGLE SUPPLIER → PDF
    // ================================================================================
    @GetMapping("/{id}/performance/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws Exception {

        SupplierPerformanceReportDTO dto = performanceService.getReportForSupplier(id);
        byte[] pdfBytes = pdfReportGenerator.generateSupplierPerformancePdf(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"supplier_performance_" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // ================================================================================
    // SINGLE SUPPLIER → EXCEL
    // ================================================================================
    @GetMapping("/{id}/performance/excel")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable Long id) throws Exception {

        SupplierPerformanceReportDTO dto = performanceService.getReportForSupplier(id);
        byte[] excelBytes = excelReportGenerator.generateSupplierPerformanceExcel(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"supplier_performance_" + id + ".xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
