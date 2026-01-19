package com.sboot.dto;

public class SupplierPerformanceResponse {
    private String supplierName;
    private long totalOrders;
    private long deliveredOrders;
    private long delayedOrders;
    private double onTimeDeliveryRate; // percentage

    // Getters and Setters
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public long getTotalOrders() {
        return totalOrders;
    }
    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }
    public long getDeliveredOrders() {
        return deliveredOrders;
    }
    public void setDeliveredOrders(long deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }
    public long getDelayedOrders() {
        return delayedOrders;
    }
    public void setDelayedOrders(long delayedOrders) {
        this.delayedOrders = delayedOrders;
    }
    public double getOnTimeDeliveryRate() {
        return onTimeDeliveryRate;
    }
    public void setOnTimeDeliveryRate(double onTimeDeliveryRate) {
        this.onTimeDeliveryRate = onTimeDeliveryRate;
    }
}
