package com.sboot.service;

import com.sboot.dto.SupplierRatingRequest;
import com.sboot.dto.SupplierRatingResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierRatingService {
    SupplierRatingResponse create(SupplierRatingRequest request);
    SupplierRatingResponse getById(Long id);
    Page<SupplierRatingResponse> getBySupplierId(Long supplierId, Pageable pageable);
    List<SupplierRatingResponse> getAllRatingsList();
    Page<SupplierRatingResponse> getAllRatings(Pageable pageable);
    SupplierRatingResponse update(Long id, SupplierRatingRequest request);
    void delete(Long id);
}
