package com.sboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sboot.entity.ProductionUnit;

@Repository
public interface ProductionUnitRepository extends JpaRepository<ProductionUnit, Long> {

    // Find all production units by category
    List<ProductionUnit> findByCategory_CategoryId(Long categoryId);

    // Find by status (e.g. "ACTIVE", "INACTIVE", "MAINTENANCE")
    List<ProductionUnit> findByStatus(String status);

    // Count available or unavailable units
    Long countByAvailable(boolean available);

    // ✅ For analytics — get total number of active production units
    @Query("SELECT COUNT(pu) FROM ProductionUnit pu WHERE pu.status = 'ACTIVE'")
    Long countActiveUnits();

    // ✅ For analytics — get total number of units by category
    @Query("SELECT pu.category.categoryName, COUNT(pu) FROM ProductionUnit pu GROUP BY pu.category.categoryName")
    List<Object[]> countUnitsByCategory();
}
