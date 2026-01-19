package com.sboot.controller;

import com.sboot.entity.SupplierPayment;
import com.sboot.service.SupplierHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliershistory")
public class SupplierHistoryController {

    private final SupplierHistoryService supplierHistoryService;

    public SupplierHistoryController(SupplierHistoryService supplierHistoryService) {
        this.supplierHistoryService = supplierHistoryService;
    }

    @GetMapping("/{supplierId}/history")
    public ResponseEntity<?> getSupplierPaymentHistory(@PathVariable Long supplierId) {

        List<SupplierPayment> payments = supplierHistoryService.getSupplierPaymentHistory(supplierId);

        if (payments.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No payment history found for Supplier ID: " + supplierId);
        }

        return ResponseEntity.ok(payments);
    }
}
