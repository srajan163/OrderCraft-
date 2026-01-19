package com.sboot.service;

import com.sboot.entity.Category;
import com.sboot.entity.Product;
import com.sboot.entity.Supplier;
import com.sboot.repository.CategoryRepository;
import com.sboot.repository.ProductRepository;
import com.sboot.repository.SuppliersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SuppliersRepository supplierRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          SuppliersRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    // CRUD
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Assign Category to Product
    public Product assignCategory(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        product.setCategory(category);
        return productRepository.save(product);
    }

    // Assign Supplier to Product
    public Product assignSupplier(Long productId, Long supplierId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();
        product.setSupplier(supplier);
        return productRepository.save(product);
    }

    // Filter Products by Category
    public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return productRepository.findByCategory(category);
    }

    // Filter Products by Supplier
    public List<Product> getProductsBySupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();
        return productRepository.findBySupplier(supplier);
    }

 // ⭐ NEW FEATURE — Get total productsQuantity
    public int getAllProductsQuantity() {
        Integer total = productRepository.getTotalProductsQuantity();
        return total != null ? total : 0;
    }
}
