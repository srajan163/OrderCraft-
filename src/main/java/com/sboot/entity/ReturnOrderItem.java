package com.sboot.entity;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
 
import jakarta.persistence.*;
 
@Entity

@Table(name = "RETURN_ORDER_ITEMS")

public class ReturnOrderItem {
 
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_order_items_seq")

    @SequenceGenerator(name = "return_order_items_seq", sequenceName = "return_order_items_seq", allocationSize = 1)

    @Column(name = "ROIID")

    private Long roiId;
 
    @Column(name = "ROIQUANTITY")

    private Integer returnQuantity;
 
    @Column(name = "ROICONDITIONNOTE")

    private String conditionNote;
 
    @ManyToOne

    @JoinColumn(name = "ROIRETURNORDERID")

    @JsonIgnore

    private ReturnOrder returnOrder;
 
    @ManyToOne

    @JoinColumn(name = "ROIPRODUCTID")

    private Product product;
 
    // Getters and Setters

    public Long getRoiId() {

        return roiId;

    }
 
    public void setRoiId(Long roiId) {

        this.roiId = roiId;

    }
 
    public Integer getReturnQuantity() {

        return returnQuantity;

    }
 
    public void setReturnQuantity(Integer returnQuantity) {

        this.returnQuantity = returnQuantity;

    }
 
    public String getConditionNote() {

        return conditionNote;

    }
 
    public void setConditionNote(String conditionNote) {

        this.conditionNote = conditionNote;

    }
 
    public ReturnOrder getReturnOrder() {

        return returnOrder;

    }
 
    public void setReturnOrder(ReturnOrder returnOrder) {

        this.returnOrder = returnOrder;

    }
 
    public Product getProduct() {

        return product;

    }
 
    public void setProduct(Product product) {

        this.product = product;

    }

}
 
 