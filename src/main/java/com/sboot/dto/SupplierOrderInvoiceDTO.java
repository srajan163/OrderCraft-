// com.sboot.dto.SupplierOrderInvoiceDTO.java
package com.sboot.dto;

import java.time.LocalDate;
import java.util.List;

public class SupplierOrderInvoiceDTO {
    private Long poId;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private String createdBy;
    private List<Item> items;
    private boolean isSupplierOrder; // ✅ Add this field

    public boolean isSupplierOrder() {
		return isSupplierOrder;
	}
	public void setSupplierOrder(boolean isSupplierOrder) {
		this.isSupplierOrder = isSupplierOrder;
	}
	public static class Item {
        private String productName;
        private int quantity;
        private double unitPrice;

        // Getters and setters
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    }

    // Getters and setters for SupplierOrderInvoiceDTO fields
    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
