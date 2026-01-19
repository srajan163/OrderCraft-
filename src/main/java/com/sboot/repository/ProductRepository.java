package com.sboot.repository;
 
import com.sboot.entity.Product;

import com.sboot.entity.Category;

import com.sboot.entity.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findBySupplier(Supplier supplier);
    
 // ⭐ NEW FEATURE — Get total sum of productsQuantity
    @Query("SELECT SUM(p.productsQuantity) FROM Product p")
    Integer getTotalProductsQuantity();

}
 