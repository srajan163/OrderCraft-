package com.sboot.repository;
 
 
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sboot.entity.InventoryTransaction;
 


public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

	InventoryTransaction save(InventoryTransaction txn);
 
    // You can add custom query methods here if needed
	@Query("SELECT t FROM InventoryTransaction t WHERE t.itTransactionDate BETWEEN :start AND :end")
    List<InventoryTransaction> findByDateRange(
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate
    );
 
}

 