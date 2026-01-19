package com.sboot.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;


public class CreatePaymentRequest {
@NotNull
private Long supplierId;
@NotBlank
private String invoiceNumber;
@NotNull
private LocalDate invoiceDate;
@NotNull
private LocalDate dueDate;
@NotNull
@DecimalMin(value = "0.00", inclusive = false)
private BigDecimal amount;


// getters/setters
public Long getSupplierId() { return supplierId; }
public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
public String getInvoiceNumber() { return invoiceNumber; }
public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
public LocalDate getInvoiceDate() { return invoiceDate; }
public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
public LocalDate getDueDate() { return dueDate; }
public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
public BigDecimal getAmount() { return amount; }
public void setAmount(BigDecimal amount) { this.amount = amount; }
}