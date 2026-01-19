package com.sboot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
	@SequenceGenerator(name = "invoice_seq", sequenceName = "INVOICE_SEQ", allocationSize = 1)
	private Long id;


    private Long orderId;
    private String billNumber; // ✅ Add this
    private double totalAmount;
    private LocalDateTime generatedAt;
    // ✅ Constructorsss
    public Invoice() {}

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
