package com.sboot.service;

import com.sboot.entity.Supplier;
import com.sboot.entity.SupplierPayment;
import com.sboot.repository.SupplierPaymentRepository;
import com.sboot.repository.SuppliersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierPaymentService {

    @Autowired
    private SupplierPaymentRepository paymentRepo;

    @Autowired
    private SuppliersRepository supplierRepo;

    /**
     * Create a payment record for a supplier.
     * Validates supplier existence, sets timestamps and status defaults.
     */
    public SupplierPayment createPayment(SupplierPayment payment) {
        // validate supplier exists
        Long sid = payment.getSupplier() != null ? payment.getSupplier().getSuppliersId() : null;
        if (sid == null) {
            throw new IllegalArgumentException("supplier must be provided");
        }
        Supplier s = supplierRepo.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        payment.setSupplier(s);
        payment.setCreatedAt(OffsetDateTime.now());
        payment.setUpdatedAt(OffsetDateTime.now());

        // ensure paidAmount not null
        if (payment.getPaidAmount() == null) {
            payment.setPaidAmount(BigDecimal.ZERO);
        }

        // normalize status based on amounts
        payment.applyPayment(BigDecimal.ZERO);
        return paymentRepo.save(payment);
    }

    /**
     * All payments for a given supplier.
     */
    public List<SupplierPayment> getPaymentsForSupplier(Long supplierId) {
        return paymentRepo.findBySupplier_SuppliersId(supplierId);
    }

    /**
     * All payments.
     */
    public List<SupplierPayment> getAllPayments() {
        return paymentRepo.findAll();
    }

    /**
     * Apply a payment amount to a payment record.
     * Enforces positive amount and switches status (PENDING -> PARTIAL -> PAID).
     */
    @Transactional
    public SupplierPayment applyPayment(Long paymentId, BigDecimal amount) {
        SupplierPayment p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment record not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        p.applyPayment(amount);
        p.setUpdatedAt(OffsetDateTime.now());
        return paymentRepo.save(p);
    }

    /**
     * Payments that are not fully paid.
     */
    public List<SupplierPayment> getOutstandingPayments() {
        // PENDING or PARTIAL
        return paymentRepo.findByStatusIn(List.of("PENDING", "PARTIAL"));
    }

    /**
     * Single payment lookup.
     */
    public Optional<SupplierPayment> getPaymentById(Long id) {
        return paymentRepo.findById(id);
    }

    /**
     * Delete a payment record.
     */
    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }
}
