package com.sboot.dto;
// hi
public class ReturnOrderItemDTO {
    private Long productId;
    private Integer quantity;
    private String conditionNote;
    private Long purchaseOrderItemId;
 
    public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}
	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}
	// Getters and Setters
//	updated
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
 
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
 
    public String getConditionNote() {
        return conditionNote;
    }
    public void setConditionNote(String conditionNote) {
        this.conditionNote = conditionNote;
    }
}
 
 
 