package com.sboot.service;

import com.sboot.dto.SupplierRatingRequest;
import com.sboot.dto.SupplierRatingResponse;
import com.sboot.entity.Supplier;
import com.sboot.entity.SupplierRating;

public class SupplierRatingMapper {

	
	// Pass Supplier object from service layer
    public static SupplierRating toEntity(SupplierRatingRequest req, Supplier supplier) {
        SupplierRating e = new SupplierRating();
        e.setSupplier(supplier);
        e.setReview(req.getReview());
        return e;
    }


    public static SupplierRatingResponse toDto(SupplierRating e) {
        SupplierRatingResponse dto = new SupplierRatingResponse();
        //dto.setId(e.getId());
        dto.setSupplierId(e.getSupplier().getSuppliersId());
        dto.setSupplierName(e.getSupplier().getSuppliersName());
        dto.setRating(e.getRating());
        dto.setReview(e.getReview());
        //dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

}
