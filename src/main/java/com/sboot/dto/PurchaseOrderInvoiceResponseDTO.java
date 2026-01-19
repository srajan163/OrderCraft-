package com.sboot.dto;

import java.util.List;

public class PurchaseOrderInvoiceResponseDTO {
    private Long orderId;
    private String userName;
    private String userEmail;
    private String userMobile;
    private String deliveryAddress;

    // Supplier details
    private String supplierName;
    private String supplierContact;
    private String supplierAddress;

    private List<OrderItemDTO> items;

    public PurchaseOrderInvoiceResponseDTO() {
    }

    public PurchaseOrderInvoiceResponseDTO(Long orderId, String userName, String userEmail, String userMobile,
                                           String deliveryAddress, String supplierName, String supplierContact,
                                           String supplierAddress, List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.deliveryAddress = deliveryAddress;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.supplierAddress = supplierAddress;
        this.items = items;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
