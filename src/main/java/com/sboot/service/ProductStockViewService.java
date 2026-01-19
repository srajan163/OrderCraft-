package com.sboot.service;
 
import com.sboot.dto.AlertDto;
import com.sboot.dto.ProductStockView;
import com.sboot.entity.Product;
import com.sboot.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class ProductStockViewService {
 
    private final ProductsRepository productRepository;
 
    public ProductStockViewService(ProductsRepository productRepository) {
        this.productRepository = productRepository;
    }
 
    public List<ProductStockView> getAllProductStockViews() {
        List<Product> products = productRepository.findAll();
 
        return products.stream().map(product -> {
            String alert = "Normal";
 
            if (product.getProductsQuantity() < product.getMinThreshold()) {
                alert = "Below Minimum Threshold";
            } else if (product.getProductsQuantity() > product.getMaxThreshold()) {
                alert = "Above Maximum Threshold";
            }
 
            return new ProductStockView(
                    product.getProductsId(),
                    product.getProductsName(),
                    product.getProductsQuantity(),
                    product.getMaxThreshold(),
                    product.getMinThreshold(),
                    alert
            );
        }).collect(Collectors.toList());
    }
 
    public ProductStockView getProductStockViewById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
 
        String alert = "Normal";
 
        if (product.getProductsQuantity() < product.getMinThreshold()) {
            alert = "Below Minimum Threshold";
        } else if (product.getProductsQuantity() > product.getMaxThreshold()) {
            alert = "Above Maximum Threshold";
        }
 
        return new ProductStockView(
                product.getProductsId(),
                product.getProductsName(),
                product.getProductsQuantity(),
                product.getMinThreshold(),
                product.getMaxThreshold(),
                alert
        );
    }
    public List<AlertDto> getLowStockAlerts() {
        List<ProductStockView> views = getAllProductStockViews();
        LocalDateTime now = LocalDateTime.now();
        return views.stream()
            .filter(v -> "Below Minimum Threshold".equalsIgnoreCase(v.getAlert()))
            .map(v -> {
                AlertDto a = new AlertDto();
                a.setLevel("WARN");
                a.setMessage("Low stock: " + v.getProductName() + " (" + v.getAvailableQuantity() + ")");
                a.setProductId(v.getProductId());
                a.setProductName(v.getProductName());
                a.setQuantity(v.getAvailableQuantity());
                a.setCreatedAt(now);
                return a;
            })
            .toList();
    }
    
    // ⭐ NEW FEATURE — Get lowest product quantity by MinThreshold
    public ProductStockView getLowestStockBelowMinThreshold() {
        List<Product> results = productRepository.findLowestStockProduct();

        if (results.isEmpty()) {
            return null;
        }

        Product product = results.get(0);

        return new ProductStockView(
                product.getProductsId(),
                product.getProductsName(),
                product.getProductsQuantity(),
                product.getMaxThreshold(),
                product.getMinThreshold(),
                "Below Minimum Threshold"
        );
    }
    
    public long getLowStockCountBelowMinThreshold() {
        return productRepository.findAllBelowMinThreshold().size();
    }

    
}
 
 
 