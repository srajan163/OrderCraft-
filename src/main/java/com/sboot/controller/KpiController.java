package com.sboot.controller;


import org.springframework.web.bind.annotation.*;

import com.sboot.service.ProductService;
import com.sboot.service.ProductStockViewService;
import com.sboot.service.SupplierService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/kpi")
@CrossOrigin
public class KpiController {

    private final ProductService productService;
    private final SupplierService supplierService;
    private final ProductStockViewService productStockViewService;


    public KpiController(ProductService productService, SupplierService supplierService, ProductStockViewService productStockViewService) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.productStockViewService = productStockViewService;

    }

    @GetMapping
    public Map<String, Object> getKpiStats() {

        Map<String, Object> response = new HashMap<>();

        long totalProducts = productService.getAllProductsQuantity();
        long lowStock = productStockViewService.getLowStockCountBelowMinThreshold();
        long suppliers = supplierService.getTotalSuppliers();//        long todayTx = 0; // pending implementation

        response.put("totalProducts", Map.of(
                "label", "Total Products",
                "value", totalProducts,
                "change", "+2.3%"
        ));

        response.put("lowStock", Map.of(
                "label", "Low Stock",
                "value", lowStock,
                "change", "+6%"
        ));

        response.put("suppliers", Map.of(
                "label", "Suppliers",
                "value", suppliers,
                "change", "—"
        ));
//
//        response.put("todayTransactions", Map.of(
//                "label", "Today Transactions",
//                "value", todayTx,
//                "change", "-1.2%"
//        ));

        return response;
    }
}
