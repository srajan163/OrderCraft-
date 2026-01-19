package com.sboot.controller;
 
import com.sboot.entity.Product;

import com.sboot.service.ProductService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController

@RequestMapping("/products")

@CrossOrigin(origins = "http://localhost:4200") 


//updated backend 

public class ProductController {
 
    private final ProductService productService;
 
    public ProductController(ProductService productService) {

        this.productService = productService;

    }
 
    // ✅ Get all products

    @GetMapping

    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());

    }
 
    // ✅ Get product by ID

    @GetMapping("/{id}")

    public ResponseEntity<Product> getProductById(@PathVariable Long id) {

        return productService.getProductById(id)

                .map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());

    }
 
    // ✅ Add new product

    @PostMapping

    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        return ResponseEntity.ok(productService.saveProduct(product));

    }
 
    // ✅ Delete product

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();

    }
 
    // ✅ Assign category

    @PutMapping("/{productId}/category/{categoryId}")

    public ResponseEntity<Product> assignCategory(@PathVariable Long productId,

                                                  @PathVariable Long categoryId) {

        return ResponseEntity.ok(productService.assignCategory(productId, categoryId));

    }
 
    // ✅ Assign supplier

    @PutMapping("/{productId}/supplier/{supplierId}")

    public ResponseEntity<Product> assignSupplier(@PathVariable Long productId,

                                                  @PathVariable Long supplierId) {

        return ResponseEntity.ok(productService.assignSupplier(productId, supplierId));

    }
 
    // ✅ Get products by category

    @GetMapping("/category/{categoryId}")

    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {

        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));

    }
 
    // ✅ Get products by supplier

    @GetMapping("/supplier/{supplierId}")

    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable Long supplierId) {

        return ResponseEntity.ok(productService.getProductsBySupplier(supplierId));

    }

}

 