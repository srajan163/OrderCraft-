package com.sboot.repository;

import com.sboot.dto.ProductionScheduleTrackingResponse;

import com.sboot.entity.ProductionSchedule;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
 
import java.time.LocalDate;

import java.util.Collection;

import java.util.List;

import java.util.Optional;
 

import com.sboot.entity.ProductionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductionScheduleRepository extends JpaRepository<ProductionSchedule, Long> {

    @Query("SELECT ps FROM ProductionSchedule ps " +
           "WHERE (:status IS NULL OR ps.status = :status) " +
           "AND (:productId IS NULL OR ps.product.productsId = :productId) " +
           "AND (:startDate IS NULL OR ps.psStartDate >= :startDate) " +
           "AND (:endDate IS NULL OR ps.psEndDate <= :endDate)")
    List<ProductionSchedule> findForReport(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status,
            @Param("productId") Long productId
    );

    @Query("SELECT ps FROM ProductionSchedule ps WHERE ps.psDeadline <= :date")
    List<ProductionSchedule> findSchedulesByDeadline(@Param("date") LocalDate date);

    Long countByStatus(String status);

    // ✅ Oracle date subtraction must use native SQL (difference in days)
    @Query(
        value = "SELECT AVG(ps_end_date - ps_start_date) FROM production_schedule " +
                "WHERE ps_end_date IS NOT NULL AND ps_start_date IS NOT NULL",
        nativeQuery = true
    )
    Double getAverageProductionDuration();

    @Query(value = "SELECT COUNT(*) FROM production_schedule WHERE status = 'COMPLETED'", nativeQuery = true)
    Long countCompletedSchedules();

    @Query(value = "SELECT COUNT(*) FROM production_schedule WHERE status = 'IN_PROGRESS'", nativeQuery = true)
    Long countOngoingSchedules();

    @Query(value = "SELECT COUNT(*) FROM production_schedule " +
                   "WHERE ps_deadline < SYSDATE AND status <> 'COMPLETED'",
           nativeQuery = true)
    Long countDelayedSchedules();
}

