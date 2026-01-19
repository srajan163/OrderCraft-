package com.sboot.controller;

import com.sboot.dto.InvoiceResponseDTO;
import com.sboot.service.InvoiceService;
import com.sboot.util.InvoicePdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate/{orderId}")
    public InvoiceResponseDTO generateInvoice(@PathVariable Long orderId) {
        return invoiceService.generateInvoice(orderId);
    }

    @GetMapping("/pdf/{orderId}")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long orderId) {
        try {
            InvoiceResponseDTO invoice = invoiceService.generateInvoice(orderId);
            byte[] pdf = InvoicePdfGenerator.generatePdf(invoice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + orderId + ".pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
