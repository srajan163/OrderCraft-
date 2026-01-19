package com.sboot.controller;
 
import com.sboot.dto.ProductionScheduleTrackingResponse;

import com.sboot.service.ProductionScheduleTrackingService;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
 
import java.util.List;

import java.util.Optional;
 
@RestController

@RequestMapping("/api/production-tracking")

public class ProductionScheduleTrackingController {
 
	@Autowired

    private ProductionScheduleTrackingService trackingService;
 
    // View all orders

    @GetMapping

    public List<ProductionScheduleTrackingResponse> getAllProductionOrders() {

        return trackingService.getAllProductionOrders();

    }
 
    // View by ID

    @GetMapping("/{id}")

    public Optional<ProductionScheduleTrackingResponse> getProductionOrderById(@PathVariable Long id) {

        return trackingService.getProductionOrderById(id);

    }

}

 