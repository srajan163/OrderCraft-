package com.sboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sboot.service.OrderFulfillmentReportService;
import com.sboot.util.OrderFulfillmentReportExportUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderFulfillmentReportController {

    @Autowired
    private OrderFulfillmentReportService reportService;

    /**
     * Request body may still contain filter fields (startDate, endDate, status, supplierId, internal).
     * The service will return a List of maps where each map represents a PurchaseOrder's properties.
     */
    @PostMapping(value = "/generate")
    public ResponseEntity<?> generateReport(@RequestBody(required = false) Map<String, Object> filter,
                                            @RequestParam(required = false, defaultValue = "pdf") String type) {
        try {
            List<Map<String, Object>> rows = reportService.getFullPurchaseOrderMaps(filter);

            if ("pdf".equalsIgnoreCase(type)) {
                byte[] pdf = OrderFulfillmentReportExportUtil.exportEntitiesPdf(rows);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=purchase_orders_report.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);
            } else if ("csv".equalsIgnoreCase(type)) {
                byte[] csv = OrderFulfillmentReportExportUtil.exportEntitiesCsv(rows);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=purchase_orders_report.csv")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(csv);
            } else {
                return ResponseEntity.ok(rows);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating report: " + ex.getMessage());
        }
    }
}
