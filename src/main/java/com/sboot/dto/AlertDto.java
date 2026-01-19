package com.sboot.dto;

import java.time.LocalDateTime;

public class AlertDto {
    private String level; // "INFO"|"WARN"|"CRITICAL"
    private String message;
    private Long productId;
    private String productName;
    private Integer quantity;
    public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	private LocalDateTime createdAt;
	public AlertDto(String level, String message, Long productId, String productName, Integer quantity,
			LocalDateTime createdAt) {
		super();
		this.level = level;
		this.message = message;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}
	public AlertDto() {
		super();
	}
    
    

    // getters / setters / constructors
    
    
}
