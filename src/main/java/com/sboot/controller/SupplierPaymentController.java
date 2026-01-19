package com.sboot.controller;

import com.sboot.entity.SupplierPayment;
import com.sboot.service.SupplierPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supplier-payments")
@CrossOrigin(origins = "*")
public class SupplierPaymentController {

    @Autowired
    private SupplierPaymentService paymentService;

    // ------------------------------
    // Create Payment
    // ------------------------------
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody SupplierPayment payment) {
        try {
            SupplierPayment saved = paymentService.createPayment(payment);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // ------------------------------
    // Get All Payments
    // ------------------------------
    @GetMapping
    public List<SupplierPayment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // ------------------------------
    // Get Payments by Supplier ID
    // ------------------------------
    @GetMapping("/supplier/{supplierId}")
    public List<SupplierPayment> getBySupplier(@PathVariable Long supplierId) {
        return paymentService.getPaymentsForSupplier(supplierId);
    }

    // ------------------------------
    // Get Outstanding Payments
    // ------------------------------
    @GetMapping("/outstanding")
    public List<SupplierPayment> getOutstanding() {
        return paymentService.getOutstandingPayments();
    }

    // ------------------------------
    // Apply Payment (Pay Invoice)
    // ------------------------------
    @PutMapping("/{paymentId}/pay")
    public ResponseEntity<?> pay(
            @PathVariable Long paymentId,
            @RequestBody Map<String, Object> body
    ) {
        try {
            if (body == null || !body.containsKey("amount") || body.get("amount") == null) {
                throw new IllegalArgumentException("'amount' is required");
            }

            BigDecimal amount = new BigDecimal(body.get("amount").toString());
            SupplierPayment updated = paymentService.applyPayment(paymentId, amount);
            return ResponseEntity.ok(updated);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // ------------------------------
    // Delete Payment
    // ------------------------------
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> delete(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok(Map.of("deleted", paymentId));
    }
}
