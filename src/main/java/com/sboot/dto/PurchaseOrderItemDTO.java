package com.sboot.dto;

public class PurchaseOrderItemDTO {
    private Long purchaseOrderItemId;
    private Long productId;
    private String productName;
    private Integer quantity;

    public PurchaseOrderItemDTO(Long purchaseOrderItemId, Long productId, String productName, Integer quantity) {
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

	public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}

	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
    
    

    // Getters and Setters
}

