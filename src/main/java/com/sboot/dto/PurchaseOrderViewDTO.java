package com.sboot.dto;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonInclude;
 
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PurchaseOrderViewDTO {

    private Long poId;

    private String supplierName;

    private String orderedBy;

    private LocalDateTime poOrderDate;

    private LocalDate poExpectedDeliveryDate;

    private String poDeliveryStatus;

    private List<ItemDTO> items;
 
    public static class ItemDTO {

        public Long itemId;

        public String productName;

        public int quantity;

        public double cost;

    }
 
    // Getters and Setters
 
    public Long getPoId() { return poId; }

    public void setPoId(Long poId) { this.poId = poId; }
 
    public String getSupplierName() { return supplierName; }

    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
 
    public String getOrderedBy() { return orderedBy; }

    public void setOrderedBy(String orderedBy) { this.orderedBy = orderedBy; }
 
    public LocalDateTime getPoOrderDate() { return poOrderDate; }

    public void setPoOrderDate(LocalDateTime poOrderDate) { this.poOrderDate = poOrderDate; }
 
    public LocalDate getPoExpectedDeliveryDate() { return poExpectedDeliveryDate; }

    public void setPoExpectedDeliveryDate(LocalDate poExpectedDeliveryDate) { this.poExpectedDeliveryDate = poExpectedDeliveryDate; }
 
    public String getPoDeliveryStatus() { return poDeliveryStatus; }

    public void setPoDeliveryStatus(String poDeliveryStatus) { this.poDeliveryStatus = poDeliveryStatus; }
 
    public List<ItemDTO> getItems() { return items; }

    public void setItems(List<ItemDTO> items) { this.items = items; }

}

 