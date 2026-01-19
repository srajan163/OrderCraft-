package com.sboot.dto;

import java.time.LocalDate;

public class ProductionReportRequest {
    private LocalDate startDate;   // filter against PS_START_DATE
    private LocalDate endDate;     // filter against PS_END_DATE
    private String psStatus;       // filter against PSSTATUS or PS_STATUS
    private Long productId;        // filter against PRODUCT_ID

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

    public String getPsStatus() {
        return psStatus;
    }
    public void setPsStatus(String psStatus) {
        this.psStatus = psStatus;
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
