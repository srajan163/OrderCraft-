package com.sboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sboot.entity.Product;
import com.sboot.entity.User;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
	
	
	// Search by product name
	@Query("SELECT p FROM Product p WHERE LOWER(p.productsName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Product> searchByName(@Param("name") String name);

    // If you want search by exact id
    List<Product> findByProductsId(Long id);

    List<Product> searchByProductsNameContainingIgnoreCase(String name);

    
    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minThreshold")
    List<Product> findLowStockProducts();
    
    // ✔ All products below MinThreshold
    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minThreshold")
    List<Product> findAllBelowMinThreshold();


    // ✔ Lowest stock among products below MinThreshold
    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minThreshold ORDER BY p.productsQuantity ASC")
    List<Product> findLowestStockProduct();


	


	
}
