//package com.sboot.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.sboot.entity.PurchaseOrder;
//
//@Repository
//public interface PurchaseOrdersRepository extends JpaRepository<PurchaseOrder, Long> {
//	List<PurchaseOrder> findByPoDeliveryStatusNot(String status);
//	List<PurchaseOrder> findByPoDeliveryStatusNotIn(List<String> statuses);
//	 
//}




package com.sboot.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sboot.entity.PurchaseOrder;
import com.sboot.entity.Supplier;

@Repository
public interface PurchaseOrdersRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByPoDeliveryStatusNot(String status);
    List<PurchaseOrder> findByPoDeliveryStatusNotIn(List<String> statuses);

    // Performance
    List<PurchaseOrder> findBySupplier(Supplier supplier);
    long countBySupplier(Supplier supplier);
    long countBySupplierAndPoDeliveryStatusIgnoreCase(Supplier supplier, String poDeliveryStatus);

    // Find overdue orders for a supplier using method derivation
    List<PurchaseOrder> findBySupplierAndPoExpectedDeliveryDateBeforeAndPoDeliveryStatusNotIgnoreCase(
            Supplier supplier, LocalDate date, String excludedStatus);

    // Equivalent query using @Query annotation
    @Query("SELECT p FROM PurchaseOrder p " +
           "WHERE p.supplier = :supplier " +
           "AND p.poExpectedDeliveryDate < :date " +
           "AND LOWER(p.poDeliveryStatus) <> LOWER(:excludedStatus)")
    List<PurchaseOrder> findOverdueOrders(
            @Param("supplier") Supplier supplier,
            @Param("date") LocalDate date,
            @Param("excludedStatus") String excludedStatus);
    
    @Query("SELECT p FROM PurchaseOrder p WHERE p.poOrderDate BETWEEN :start AND :end")
    List<PurchaseOrder> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
