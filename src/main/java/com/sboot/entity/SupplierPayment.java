package com.sboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "SUPPLIER_PAYMENTS")
public class SupplierPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_payments_seq")
    @SequenceGenerator(name = "supplier_payments_seq", sequenceName = "SUPPLIER_PAYMENTS_SEQ", allocationSize = 1)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "SUPPLIERSID", nullable = false)
    private Supplier supplier;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICE_DATE")
    private LocalDate invoiceDate;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "PAID_AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "STATUS", length = 32)
    private String status = "PENDING"; // PENDING, PARTIAL, PAID

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;

    // constructors, getters and settersss

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    // helper to apply payment
    public void applyPayment(BigDecimal payment) {
        if (payment == null) return;
        this.paidAmount = this.paidAmount.add(payment);
        if (this.paidAmount.compareTo(this.amount) >= 0) {
            this.status = "PAID";
            this.paidAmount = this.amount;
        } else if (this.paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.status = "PARTIAL";
        } else {
            this.status = "PENDING";
        }
        this.updatedAt = OffsetDateTime.now();
    }
}
