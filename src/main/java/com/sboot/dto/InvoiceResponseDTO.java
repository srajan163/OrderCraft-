package com.sboot.dto;

import java.util.List;

public class InvoiceResponseDTO {
    private Long orderId;
    private String billingAddress;
    private String phoneNumber;
    private String customerName;
    private String customerEmail;
    private List<OrderItemDTO> items;

    public InvoiceResponseDTO() {}

    public InvoiceResponseDTO(Long orderId, String billingAddress, String phoneNumber, String customerName,
                              String customerEmail, List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.billingAddress = billingAddress;
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.items = items;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
