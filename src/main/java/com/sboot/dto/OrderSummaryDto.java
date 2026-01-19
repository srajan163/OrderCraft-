package com.sboot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDto {
    private long totalOrders;
    private long pendingOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private List<SimpleOrderDto> recent;
    // last N orders

    // getters / setters / constructors

    public static class SimpleOrderDto {
        public SimpleOrderDto() {
			super();
		}
		public SimpleOrderDto(Long poId, String supplierName, String orderedBy, String status,
				LocalDateTime orderDate) {
			super();
			this.poId = poId;
			this.supplierName = supplierName;
			this.orderedBy = orderedBy;
			this.status = status;
			this.orderDate = orderDate;
		}
		public Long getPoId() {
			return poId;
		}
		public void setPoId(Long poId) {
			this.poId = poId;
		}
		public String getSupplierName() {
			return supplierName;
		}
		public void setSupplierName(String supplierName) {
			this.supplierName = supplierName;
		}
		public String getOrderedBy() {
			return orderedBy;
		}
		public void setOrderedBy(String orderedBy) {
			this.orderedBy = orderedBy;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public LocalDateTime getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(LocalDateTime orderDate) {
			this.orderDate = orderDate;
		}
		public Long poId;
        public String supplierName;
        public String orderedBy;
        public String status;
        public LocalDateTime orderDate;
    }

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public long getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(long pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public long getShippedOrders() {
		return shippedOrders;
	}

	public void setShippedOrders(long shippedOrders) {
		this.shippedOrders = shippedOrders;
	}

	public long getDeliveredOrders() {
		return deliveredOrders;
	}

	public void setDeliveredOrders(long deliveredOrders) {
		this.deliveredOrders = deliveredOrders;
	}

	public List<SimpleOrderDto> getRecent() {
		return recent;
	}

	public void setRecent(List<SimpleOrderDto> recent) {
		this.recent = recent;
	}

    // getters and setters ...
}
