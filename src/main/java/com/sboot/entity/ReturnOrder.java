package com.sboot.entity;
 
import jakarta.persistence.*;

import java.util.Date;

import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
 
@Entity

@Table(name = "RETURN_ORDERS")

public class ReturnOrder {
 
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_orders_seq")

    @SequenceGenerator(name = "return_orders_seq", sequenceName = "return_orders_seq", allocationSize = 1)

    @Column(name = "ROID")

    private Long roId;
 
    @Column(name = "RORETURNDATE")

    private Date roReturnDate;
 
    @Column(name = "RORETURNREASON")

    private String roReturnReason;
 
    @Column(name = "ROSTATUS")

    private String roStatus;
 
    @ManyToOne

    @JoinColumn(name = "ROPURCHASEORDERID")

    private PurchaseOrder purchaseOrder;
 
    @ManyToOne

    @JsonIgnore

    @JoinColumn(name = "RORETURNEDBY")

    private User returnedBy;
 
    @OneToMany(mappedBy = "returnOrder", cascade = CascadeType.ALL)

    private List<ReturnOrderItem> items;
 
    // Getters and Setters

    public Long getRoId() {

        return roId;

    }
 
    public void setRoId(Long roId) {

        this.roId = roId;

    }
 
    public Date getRoReturnDate() {

        return roReturnDate;

    }
 
    public void setRoReturnDate(Date roReturnDate) {

        this.roReturnDate = roReturnDate;

    }
 
    public String getRoReturnReason() {

        return roReturnReason;

    }
 
    public void setRoReturnReason(String roReturnReason) {

        this.roReturnReason = roReturnReason;

    }
 
    public String getRoStatus() {

        return roStatus;

    }
 
    public void setRoStatus(String roStatus) {

        this.roStatus = roStatus;

    }
 
    public PurchaseOrder getPurchaseOrder() {

        return purchaseOrder;

    }
 
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {

        this.purchaseOrder = purchaseOrder;

    }
 
    public User getReturnedBy() {

        return returnedBy;

    }
 
    public void setReturnedBy(User returnedBy) {

        this.returnedBy = returnedBy;

    }
 
    public List<ReturnOrderItem> getItems() {

        return items;

    }
 
    public void setItems(List<ReturnOrderItem> items) {

        this.items = items;

    }

    @PrePersist

    protected void onCreate() {

        this.roReturnDate = new Date(); // Automatically sets current local date and time

    }

}

 