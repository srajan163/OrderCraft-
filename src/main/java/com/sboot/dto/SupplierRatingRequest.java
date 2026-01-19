package com.sboot.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class SupplierRatingRequest {

    @NotNull
    private Long supplierId;

    @Size(max = 2000)
    private String review;

    @NotNull
    @Size(min = 1, message = "At least one answer is required")
    private List<Integer> answers; // e.g. [4, 5, 3, 4]

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
}
