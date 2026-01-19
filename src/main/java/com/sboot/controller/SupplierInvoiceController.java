package com.sboot.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sboot.dto.SupplierInvoiceResponseDTO;
import com.sboot.service.SupplierInvoiceServiceImpl;
import com.sboot.util.SupplierInvoicePdfGenerator;

@RestController
@RequestMapping("/api/supplier-invoices")
public class SupplierInvoiceController {

    @Autowired
    private SupplierInvoiceServiceImpl supplierInvoiceService;

    @PostMapping("/{purchaseOrderId}/generate")
    public ResponseEntity<?> generateInvoices(@PathVariable Long purchaseOrderId) {
        List<SupplierInvoiceResponseDTO> invoices = supplierInvoiceService.generateSupplierInvoices(purchaseOrderId);

        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.badRequest().body("No invoice items found.");
        }

        try {
            byte[] pdf = SupplierInvoicePdfGenerator.generatePdf(invoices);
            String fileName = "supplier_invoice_PO" + purchaseOrderId + ".pdf";

            // ✅ Save to disk (optional)
            Path path = Paths.get("invoices/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, pdf);

            // ✅ Return the PDF in the HTTP response
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error generating invoice PDF: " + e.getMessage());
        }
    }
}
