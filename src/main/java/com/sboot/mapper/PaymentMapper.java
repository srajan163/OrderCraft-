package com.sboot.mapper;


import com.sboot.dto.PaymentResponse;
import com.sboot.entity.SupplierPayment;


public class PaymentMapper {
public static PaymentResponse toResponse(SupplierPayment sp) {
PaymentResponse r = new PaymentResponse();
r.setPaymentId(sp.getPaymentId());
if (sp.getSupplier() != null) {
r.setSupplierId(sp.getSupplier().getSuppliersId());
r.setSupplierName(sp.getSupplier().getSuppliersName());
}
r.setInvoiceNumber(sp.getInvoiceNumber());
r.setInvoiceDate(sp.getInvoiceDate());
r.setDueDate(sp.getDueDate());
r.setAmount(sp.getAmount());
r.setPaidAmount(sp.getPaidAmount());
r.setStatus(sp.getStatus());
r.setCreatedAt(sp.getCreatedAt());
r.setUpdatedAt(sp.getUpdatedAt());
return r;
}
}