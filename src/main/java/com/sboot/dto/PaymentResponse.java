package com.sboot.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;


public class PaymentResponse {
private Long paymentId;
private Long supplierId;
private String supplierName;
private String invoiceNumber;
private LocalDate invoiceDate;
private LocalDate dueDate;
private BigDecimal amount;
private BigDecimal paidAmount;
private String status;
private OffsetDateTime createdAt;
private OffsetDateTime updatedAt;


// getters/setters
public Long getPaymentId() { return paymentId; }
public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
public Long getSupplierId() { return supplierId; }
public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
public String getSupplierName() { return supplierName; }
public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
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
}