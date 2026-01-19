package com.sboot.dto;
 
import java.time.LocalDate;
 
public class InventoryReportFilterDTO {
    private Long categoryId;        // Optional category filter
    private LocalDate startDate;    // Optional start date for last updated
    private LocalDate endDate;      // Optional end date for last updated
 
    public Long getCategoryId() {
        return categoryId;
    }
 
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
 
    public LocalDate getStartDate() {
        return startDate;
    }
 
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
 
    public LocalDate getEndDate() {
        return endDate;
    }
 
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
 
 