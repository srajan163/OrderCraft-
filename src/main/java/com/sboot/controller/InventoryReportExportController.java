//inventory Report
package com.sboot.controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sboot.dto.InventoryReportFilterDTO;
import com.sboot.dto.InventoryTransactionReportDTO;
import com.sboot.dto.StockReportDTO;
import com.sboot.service.InventoryReportService;
import com.sboot.util.InventoryReportExportUtil;
import java.util.List;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/api/reports/export")
public class InventoryReportExportController {
    private final InventoryReportService reportService;
    public InventoryReportExportController(InventoryReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/stock")
    //@PreAuthorize("hasRole('IM')")
    public ResponseEntity<byte[]> exportStockReport(
            @RequestParam(name = "exportType", defaultValue = "pdf") String type,
            @RequestBody InventoryReportFilterDTO filter) throws Exception {
        List<StockReportDTO> report = reportService.getStockReport(filter);
        byte[] data;
        String filename;
        if ("csv".equalsIgnoreCase(type)) {
            data = InventoryReportExportUtil.exportStockReportCsv(report);
            filename = "stock_report.csv";
        } else {
            data = InventoryReportExportUtil.exportStockReportPdf(report);
            filename = "stock_report.pdf";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType("csv".equalsIgnoreCase(type) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", filename);
        return ResponseEntity.ok().headers(headers).body(data);
    }


    @PostMapping("/transactions")
    //@PreAuthorize("hasRole('IM')")
    public ResponseEntity<byte[]> exportTransactionReport(
            @RequestParam(name = "exportType", defaultValue = "pdf") String type,
            @RequestBody InventoryReportFilterDTO filter) throws Exception {
        // Make a new service method to accept filters for transactions
        List<InventoryTransactionReportDTO> report = reportService.getTransactionReport(filter);
        byte[] data;
        String filename;
        if ("csv".equalsIgnoreCase(type)) {
            data = InventoryReportExportUtil.exportTransactionReportCsv(report);
            filename = "transaction_report.csv";
        } else {
            data = InventoryReportExportUtil.exportTransactionReportPdf(report);
            filename = "transaction_report.pdf";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType("csv".equalsIgnoreCase(type) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", filename);
        return ResponseEntity.ok().headers(headers).body(data);
    }

}

 