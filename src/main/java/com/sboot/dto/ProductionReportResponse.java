package com.sboot.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductionReportResponse {
    private Long psId;          // PSID
    private Long productId;     // PRODUCT_ID
    private String productName; // Join from PRODUCT table if needed
    private LocalDate psStartDate; // PS_START_DATE
    private LocalDate psDeadline;  // PSDEADLINE
    private LocalDate psEndDate;   // PS_END_DATE
    private Integer psQuantity;    // PS_QUANTITY
    private String psStatus;       // PSSTATUS or PS_STATUS

    public Long getPsId() {
        return psId;
    }
    public void setPsId(Long psId) {
        this.psId = psId;
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getPsStartDate() {
        return psStartDate;
    }
    public void setPsStartDate(LocalDate psStartDate) {
        this.psStartDate = psStartDate;
    }

    public LocalDate getPsDeadline() {
        return psDeadline;
    }
    public void setPsDeadline(LocalDate psDeadline) {
        this.psDeadline = psDeadline;
    }

    public LocalDate getPsEndDate() {
        return psEndDate;
    }
    public void setPsEndDate(LocalDate psEndDate) {
        this.psEndDate = psEndDate;
    }

    public Integer getPsQuantity() {
        return psQuantity;
    }
    public void setPsQuantity(Integer psQuantity) {
        this.psQuantity = psQuantity;
    }

    public String getPsStatus() {
        return psStatus;
    }
    public void setPsStatus(String psStatus) {
        this.psStatus = psStatus;
    }
}
