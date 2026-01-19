package com.sboot.service;
 
import com.sboot.dto.ProductionScheduleTrackingResponse;

import com.sboot.entity.ProductionSchedule;

import com.sboot.entity.Product;

import com.sboot.repository.ProductionScheduleRepository;

import com.sboot.repository.ProductsRepository;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import java.util.List;

import java.util.Optional;

import java.util.stream.Collectors;
 
@Service

public class ProductionScheduleTrackingService {
 
    @Autowired

    private ProductionScheduleRepository scheduleRepository;
 
    @Autowired

    private ProductsRepository productRepository;
 
    private ProductionScheduleTrackingResponse mapToTrackingResponse(ProductionSchedule ps) {

        ProductionScheduleTrackingResponse dto = new ProductionScheduleTrackingResponse();

        dto.setScheduleId(ps.getPsId());
 
        // Safely fetch product name

        Product product = ps.getProduct();

        if (product == null && ps.getPsId() != null) {

            product = productRepository.findById(ps.getPsId()).orElse(null);

        }

        String productName = (product != null) ? product.getProductsName() : "Unknown";

        dto.setProductId(product != null ? product.getProductsId() : null);

        dto.setProductName(productName);

        dto.setQuantity(ps.getPsQuantity());
 
        // Correct date mapping - keep LocalDateTime directly

        dto.setStartDate(ps.getPsStartDate());

        dto.setEndDate(ps.getPsEndDate());
 
        dto.setStatus(ps.getStatus());

        return dto;

    }
 
    // View all production orders

    public List<ProductionScheduleTrackingResponse> getAllProductionOrders() {

        return scheduleRepository.findAll()

                .stream()

                .map(this::mapToTrackingResponse)

                .collect(Collectors.toList());

    }
 
    // View production order by ID

    public Optional<ProductionScheduleTrackingResponse> getProductionOrderById(Long id) {

        return scheduleRepository.findById(id)

                .map(this::mapToTrackingResponse);

    }

}
 
 