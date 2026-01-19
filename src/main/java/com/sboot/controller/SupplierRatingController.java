package com.sboot.controller;

import com.sboot.dto.SupplierRatingRequest;
import com.sboot.dto.SupplierRatingResponse;
import com.sboot.service.SupplierRatingService;
import com.sboot.service.SupplierRatingServiceImpl;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class SupplierRatingController {

    private final SupplierRatingService service;
    
    @Autowired
    private SupplierRatingServiceImpl supplierRatingService;
    
    public SupplierRatingController(SupplierRatingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SupplierRatingResponse> create(@Valid @RequestBody SupplierRatingRequest request) {
        SupplierRatingResponse resp = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierRatingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierRatingResponse>> listBySupplier(
            @RequestParam(value = "supplierId", required = false) Long supplierId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierRatingResponse> pageResp;

        if (supplierId != null) {
            pageResp = service.getBySupplierId(supplierId, pageable);
        } else {
            pageResp = service.getAllRatings(pageable);
        }

        return ResponseEntity.ok(pageResp);
    }



    @PutMapping("/{id}")
    public ResponseEntity<SupplierRatingResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody SupplierRatingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<SupplierRatingResponse>> getAllRatingsList() {
        List<SupplierRatingResponse> ratings = service.getAllRatingsList();
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/latest")
    public ResponseEntity<List<SupplierRatingResponse>> getLatestRatings() {
        List<SupplierRatingResponse> response = supplierRatingService.getLatestSupplierRatings();
        return ResponseEntity.ok(response);
    }
}
