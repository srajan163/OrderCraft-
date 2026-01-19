package com.sboot.dto;

import java.time.LocalDate;

public class SupplierRatingResponse {

    //private Long id;
    private Long supplierId;
    private String supplierName;
    private Integer rating;
    private String review;
    //private LocalDate createdAt;
    private LocalDate updatedAt;

    // ===== Getters and Setters =====

//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }

//    public LocalDate getCreatedAt() {
//        return createdAt;
//    }
//    public void setCreatedAt(LocalDate createdAt) {
//        this.createdAt = createdAt;
//    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
