package com.sboot.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "PURCHASE_ORDER_ITEMS")
public class PurchaseOrderItem {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_item_seq")
    @SequenceGenerator(name = "purchase_order_item_seq", sequenceName = "PURCHASE_ORDER_ITEM_SEQ", allocationSize = 1)
    @Column(name = "POIID")
    private Long poiId;
    
    @Column(name = "POIIS_RETURNED")
    private boolean isReturned = false;
 
 
	public boolean isReturned() {
		return isReturned;
	}
 
	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
    
    @Column(name = "POIQUANTITY")
    private Integer poiQuantity;
    
    @Column(name = "POICOST")
    private Float poiCost;
    


    @ManyToOne
    @JoinColumn(name = "POIPURCHASEORDERID", referencedColumnName = "POID")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "POIPRODUCTID", referencedColumnName = "PRODUCTSID")
    private Product product;
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "RWID")
    private RawMaterial rawMaterial;
    
    
    
 

	public RawMaterial getRawMaterial() {
		return rawMaterial;
	}

	public void setRawMaterial(RawMaterial rawMaterial) {
		this.rawMaterial = rawMaterial;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public Integer getPoiQuantity() {
		return poiQuantity;
	}

	public void setPoiQuantity(Integer poiQuantity) {
		this.poiQuantity = poiQuantity;
	}

	public Float getPoiCost() {
		return poiCost;
	}

	public void setPoiCost(Float poiCost) {
		this.poiCost = poiCost;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
    
    
}

