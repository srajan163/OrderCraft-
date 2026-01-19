package com.sboot.dto;

import java.time.LocalDate;
import java.util.Map;

public class SupplierPerformanceReportDTO {
    private Long supplierId;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;
    private Double supplierRatingField; // value from Supplier.suppliersRating (optional)

    private double averageRating;
    private long totalReviews;
    private RatingSummary latestReview; // contains rating, review text, date

    // breakdown: key = rating value (1..5), value = count
    private Map<Integer, Long> ratingCounts;
    private Map<Integer, Double> ratingPercentages; // 1..5 -> percent

    public SupplierPerformanceReportDTO() {}

    // getters / setters

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getSupplierEmail() { return supplierEmail; }
    public void setSupplierEmail(String supplierEmail) { this.supplierEmail = supplierEmail; }
    public String getSupplierPhone() { return supplierPhone; }
    public void setSupplierPhone(String supplierPhone) { this.supplierPhone = supplierPhone; }
    public Double getSupplierRatingField() { return supplierRatingField; }
    public void setSupplierRatingField(Double supplierRatingField) { this.supplierRatingField = supplierRatingField; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(long totalReviews) { this.totalReviews = totalReviews; }
    public RatingSummary getLatestReview() { return latestReview; }
    public void setLatestReview(RatingSummary latestReview) { this.latestReview = latestReview; }
    public Map<Integer, Long> getRatingCounts() { return ratingCounts; }
    public void setRatingCounts(Map<Integer, Long> ratingCounts) { this.ratingCounts = ratingCounts; }
    public Map<Integer, Double> getRatingPercentages() { return ratingPercentages; }
    public void setRatingPercentages(Map<Integer, Double> ratingPercentages) { this.ratingPercentages = ratingPercentages; }

    public static class RatingSummary {
        private Integer rating;
        private String review;
        private LocalDate date;

        public RatingSummary() {}
        public RatingSummary(Integer rating, String review, LocalDate date) {
            this.rating = rating;
            this.review = review;
            this.date = date;
        }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getReview() { return review; }
        public void setReview(String review) { this.review = review; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
    }
}
