package com.sboot.dto;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
 
public class ProductionScheduleTrackingResponse {
    private Long scheduleId;
    private Long productId;
    private String productName;
    private Integer quantity;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status;
     
    
    public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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

	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate localDate) {
		this.startDate = localDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate localDate) {
		this.endDate = localDate;

	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
    // getters and setters
}
 
 