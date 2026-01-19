package com.sboot.controller;
 
import com.sboot.dto.ProductStockView;
import com.sboot.service.ProductStockViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/products")

public class ProductStockViewController {
 
    private final ProductStockViewService productStockViewService;
 
    public ProductStockViewController(ProductStockViewService productStockViewService) {
        this.productStockViewService = productStockViewService;
    }
 
    @GetMapping("/stock-view")
    public ResponseEntity<List<ProductStockView>> getAllProductsStockView() {
        return ResponseEntity.ok(productStockViewService.getAllProductStockViews());
    }
 
    @GetMapping("/{id}/stock-view")
    public ResponseEntity<ProductStockView> getProductStockView(@PathVariable Long id) {
        return ResponseEntity.ok(productStockViewService.getProductStockViewById(id));
    }
    
    @GetMapping("/products/lowest-stock")
    public ProductStockView getLowestStock() {
        return productStockViewService.getLowestStockBelowMinThreshold();
    }
}

 
 