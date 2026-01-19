package com.sboot.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;


public class ApplyPaymentRequest {
@NotNull
@DecimalMin(value = "0.00", inclusive = false)
private BigDecimal amount;


public BigDecimal getAmount() { return amount; }
public void setAmount(BigDecimal amount) { this.amount = amount; }
}