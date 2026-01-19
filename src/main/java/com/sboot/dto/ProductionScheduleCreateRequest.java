package com.sboot.dto;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
 
public class ProductionScheduleCreateRequest {
    private Long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer quantity;
    private String performedBy;
    
	@Override
	public String toString() {
		return "ProductionScheduleCreateRequest [productId=" + productId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", quantity=" + quantity + ", performedBy=" + performedBy + "]";
	}
	public ProductionScheduleCreateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductionScheduleCreateRequest(Long productId, LocalDateTime startDate, LocalDateTime endDate, Integer quantity,
			String performedBy) {
		super();
		this.productId = productId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.quantity = quantity;
		this.performedBy = performedBy;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime localDateTime) {
		this.startDate = localDateTime;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime localDateTime) {
		this.endDate = localDateTime;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
 
    // getters and setters
}
 
 