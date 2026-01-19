package com.sboot.repository;

import com.sboot.entity.SupplierRating;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRatingRepository extends JpaRepository<SupplierRating, Long> {

    @Query(value = """
            SELECT * FROM (
                SELECT sr.*, ROWNUM rnum
                FROM supplier_ratings sr
                WHERE sr.supplier_id = :supplierId
                AND ROWNUM <= :end
            ) 
            WHERE rnum >= :start
            """,
            nativeQuery = true)
    List<SupplierRating> findBySupplierIdPaged(
            @Param("supplierId") Long supplierId,
            @Param("start") int start,
            @Param("end") int end
    );
    
    @Query(value = """
            SELECT * FROM (
                SELECT sr.*, ROWNUM rnum
                FROM supplier_ratings sr
                WHERE ROWNUM <= :end
            ) 
            WHERE rnum >= :start
            """,
            nativeQuery = true)
    List<SupplierRating> findAllPaged(
            @Param("start") int start,
            @Param("end") int end
    );

    @Query(value = "SELECT COUNT(*) FROM supplier_ratings", nativeQuery = true)
    long countAllRatings();
    
    @Query(value = "SELECT COUNT(*) FROM supplier_ratings WHERE supplier_id = :supplierId", nativeQuery = true)
    long countBySupplierId(@Param("supplierId") Long supplierId);
    
    List<SupplierRating> findAll();
    
    @Query(
            value = """
            SELECT *
            FROM (
                SELECT sr.*, 
                       ROW_NUMBER() OVER (PARTITION BY sr.supplier_id ORDER BY sr.updated_at DESC) AS rn
                FROM supplier_ratings sr
            )
            WHERE rn = 1
            """,
            nativeQuery = true
        )
        List<SupplierRating> findLatestRatingsPerSupplier();
    
 // Get all ratings for a supplier
    List<SupplierRating> findBySupplier_SuppliersId(Long supplierId);

    // Get all ratings for a supplier ordered by createdAt descending
    List<SupplierRating> findBySupplier_SuppliersIdOrderByCreatedAtDesc(Long supplierId);

    // Count ratings for a supplier with a specific rating
    long countBySupplier_SuppliersIdAndRating(Long supplierId, int rating);
}

