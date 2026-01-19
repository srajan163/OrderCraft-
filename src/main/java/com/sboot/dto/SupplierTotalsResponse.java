package com.sboot.dto;


import java.math.BigDecimal;


public class SupplierTotalsResponse {
private Long supplierId;
private BigDecimal totalInvoiced;
private BigDecimal totalPaid;
private BigDecimal totalOutstanding;


public SupplierTotalsResponse(Long supplierId, BigDecimal totalInvoiced, BigDecimal totalPaid) {
this.supplierId = supplierId;
this.totalInvoiced = totalInvoiced;
this.totalPaid = totalPaid;
this.totalOutstanding = totalInvoiced.subtract(totalPaid);
}


public Long getSupplierId() { return supplierId; }
public BigDecimal getTotalInvoiced() { return totalInvoiced; }
public BigDecimal getTotalPaid() { return totalPaid; }
public BigDecimal getTotalOutstanding() { return totalOutstanding; }
}