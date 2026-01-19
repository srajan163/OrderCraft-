package com.sboot.dto;

import java.time.LocalDateTime;

public class ProductionTimelineResponse {
   private String status;
   private String updatedBy;
   private LocalDateTime updatedAt;
   private String remarks;
   private int quantity;

   public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	// Getters and Setters
   public String getStatus() { return status; }
   public void setStatus(String status) { this.status = status; }

   public String getUpdatedBy() { return updatedBy; }
   public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

   public LocalDateTime getUpdatedAt() { return updatedAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

   public String getRemarks() { return remarks; }
   public void setRemarks(String remarks) { this.remarks = remarks; }
}

