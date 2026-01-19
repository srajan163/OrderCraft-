package com.sboot.dto;

import java.util.List;

public class PurchaseOrderDTO {

    private Long poId;
    private String userId;
    private List<PurchaseOrderItemDTO> items;

    // Constructor
    public PurchaseOrderDTO(Long poId, String userId, List<PurchaseOrderItemDTO> items) {
        this.poId = poId;
        this.userId = userId;
        this.items = items;
    }

    // Getters and setters
    public Long getPoId() {
        return poId;
    }

    public void setPoId(Long poId) {
        this.poId = poId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<PurchaseOrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItemDTO> items) {
        this.items = items;
    }
}
