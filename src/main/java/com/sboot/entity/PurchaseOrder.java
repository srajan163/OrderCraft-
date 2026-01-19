//package com.sboot.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "PURCHASE_ORDERS")
//public class PurchaseOrder {
//	
//	
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_seq")
//    @SequenceGenerator(name = "purchase_order_seq", sequenceName = "PURCHASE_ORDER_SEQ", allocationSize = 1)
//    @Column(name = "POID")
//    private Long poId;
//    
//    @Column(name = "POORDERDATE")
//    private LocalDateTime poOrderDate;
//
//    
//    @Column(name = "POEXPECTEDDELIVERY_DATE")
//    private LocalDate poExpectedDelivery_date;
//    
//    @Column(name = "PODELIVERYSTATUS")
//    private String poDeliveryStatus;
//    @ManyToOne
//    
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//    @ManyToOne
//    @JoinColumn(name = "POSUPPLIERID", referencedColumnName = "SUPPLIERSID")
//    private Supplier supplier;
//    
//
//
//	@ManyToOne
//    @JoinColumn(name = "POUSERID", referencedColumnName = "USERID")
//    private User user;
//	
//	@Column(name = "POPAYMENTSTATUS")
//    private String poPaymentStatus;
//	
//    public String getPoPaymentStatus() {
//		return poPaymentStatus;
//	}
//
//	public void setPoPaymentStatus(String poPaymentStatus) {
//		this.poPaymentStatus = poPaymentStatus;
//	}
//
//	public String getPoPaymentMethod() {
//		return poPaymentMethod;
//	}
//
//	public void setPoPaymentMethod(String poPaymentMethod) {
//		this.poPaymentMethod = poPaymentMethod;
//	}
//
//	@Column(name = "POPAYMENTMETHOD")
//    private String poPaymentMethod;
//    
//    
//    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<PurchaseOrderItem> items;
//
//
//	public List<PurchaseOrderItem> getItems() {
//		return items;
//	}
//
//	public void setItems(List<PurchaseOrderItem> items) {
//		this.items = items;
//	}
//
//	public Long getPoId() {
//		return poId;
//	}
//
//	public void setPoId(Long poId) {
//		this.poId = poId;
//	}
//
//	public LocalDateTime getPoOrderDate() {
//		return poOrderDate;
//	}
//
//	public void setPoOrderDate(LocalDateTime poOrderDate) {
//		this.poOrderDate = poOrderDate;
//	}
//
//	
//	public LocalDate getPoExpectedDelivery_date() {
//		return poExpectedDelivery_date;
//	}
//
//	public void setPoExpectedDelivery_date(LocalDate localDate) {
//		this.poExpectedDelivery_date = localDate;
//	}
//
//	public String getPoDeliveryStatus() {
//		return poDeliveryStatus;
//	}
//
//	public void setPoDeliveryStatus(String poDeliveryStatus) {
//		this.poDeliveryStatus = poDeliveryStatus;
//	}
//
//	public Supplier getSupplier() {
//		return supplier;
//	}
//
//	public void setSupplier(Supplier supplier) {
//		this.supplier = supplier;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
//
//	
//
//
//
//	
//    
//}



package com.sboot.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PURCHASE_ORDERS")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_seq")
    @SequenceGenerator(name = "purchase_order_seq", sequenceName = "PURCHASE_ORDER_SEQ", allocationSize = 1)
    @Column(name = "POID")
    private Long poId;

    @Column(name = "POORDERDATE")
    private LocalDateTime poOrderDate;

    @Column(name = "POEXPECTEDDELIVERY_DATE")
    private LocalDate poExpectedDeliveryDate; // renamed to camelCase

    @Column(name = "PODELIVERYSTATUS")
    private String poDeliveryStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "POSUPPLIERID", referencedColumnName = "SUPPLIERSID")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "POUSERID", referencedColumnName = "USERID")
    private User user;

    @Column(name = "POPAYMENTSTATUS")
    private String poPaymentStatus;

    @Column(name = "POPAYMENTMETHOD")
    private String poPaymentMethod;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PurchaseOrderItem> items;

    // Getters and Setters
    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }

    public LocalDateTime getPoOrderDate() { return poOrderDate; }
    public void setPoOrderDate(LocalDateTime poOrderDate) { this.poOrderDate = poOrderDate; }

    public LocalDate getPoExpectedDeliveryDate() { return poExpectedDeliveryDate; }
    public void setPoExpectedDeliveryDate(LocalDate poExpectedDeliveryDate) { this.poExpectedDeliveryDate = poExpectedDeliveryDate; }

    public String getPoDeliveryStatus() { return poDeliveryStatus; }
    public void setPoDeliveryStatus(String poDeliveryStatus) { this.poDeliveryStatus = poDeliveryStatus; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPoPaymentStatus() { return poPaymentStatus; }
    public void setPoPaymentStatus(String poPaymentStatus) { this.poPaymentStatus = poPaymentStatus; }

    public String getPoPaymentMethod() { return poPaymentMethod; }
    public void setPoPaymentMethod(String poPaymentMethod) { this.poPaymentMethod = poPaymentMethod; }

    public List<PurchaseOrderItem> getItems() { return items; }
    public void setItems(List<PurchaseOrderItem> items) { this.items = items; }
}

