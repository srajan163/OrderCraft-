package com.sboot.repository;

import com.sboot.entity.ProductRawMaterialMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRawMaterialMappingRepository extends JpaRepository<ProductRawMaterialMapping, Long> {
    List<ProductRawMaterialMapping> findByProduct_ProductsId(Long productId);
}
