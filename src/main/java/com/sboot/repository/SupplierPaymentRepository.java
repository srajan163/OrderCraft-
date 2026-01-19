package com.sboot.repository;


import com.sboot.entity.SupplierPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {


List<SupplierPayment> findBySupplier_SuppliersId(Long supplierId);


List<SupplierPayment> findByStatusIn(Collection<String> statuses);


Page<SupplierPayment> findAll(Pageable pageable);


@Query("SELECT COALESCE(SUM(sp.amount), 0) FROM SupplierPayment sp WHERE sp.supplier.suppliersId = :supplierId")
BigDecimal sumInvoicedBySupplier(@Param("supplierId") Long supplierId);


@Query("SELECT COALESCE(SUM(sp.paidAmount), 0) FROM SupplierPayment sp WHERE sp.supplier.suppliersId = :supplierId")
BigDecimal sumPaidBySupplier(@Param("supplierId") Long supplierId);


@Query("SELECT sp FROM SupplierPayment sp WHERE (sp.status <> 'PAID') AND sp.dueDate < :asOf")
List<SupplierPayment> findOverdue(@Param("asOf") LocalDate asOf);
}