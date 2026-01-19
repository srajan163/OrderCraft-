package com.sboot.dto;

import java.util.List;

public class PurchaseOrderInternalRequest {

    private String customerName;
    private String customerEmail;
    private String customerMobile;
    private String customerAddress;

    private List<OrderforCustomer> items;

    // Getters and Setters

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrderforCustomer> getItems() {
        return items;
    }

    public void setItems(List<OrderforCustomer> items) {
        this.items = items;
    }
}
