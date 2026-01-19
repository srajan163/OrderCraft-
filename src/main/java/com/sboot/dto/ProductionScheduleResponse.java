package com.sboot.dto;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
 
public class ProductionScheduleResponse {
    private Long scheduleId;
    private Long productId;
    private String productName;
    private LocalDateTime startDate;
    public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private LocalDateTime endDate;
    private Integer quantity;
    private String status;
 
    // getters and setters
}
 
 