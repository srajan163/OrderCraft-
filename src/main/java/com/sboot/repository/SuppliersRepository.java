package com.sboot.repository;

import com.sboot.entity.Supplier;
import com.sboot.entity.SupplierRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuppliersRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findBySuppliersName(String suppliersName);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.suppliersName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Supplier> searchByName(@Param("keyword") String keyword);

    Page<Supplier> findAll(Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.suppliersContactPerson) LIKE LOWER(CONCAT('%', :contact, '%'))")
    List<Supplier> findByContactPerson(@Param("contact") String contact);

    Optional<Supplier> findBySuppliersEmail(String suppliersEmail);

    // Native SQL for counting suppliers by city
    @Query(
        value = "SELECT COUNT(*) " +
                "FROM SUPPLIERS s " +
                "JOIN ADDRESS a ON s.SUPPLIERSADDRESSID = a.ADDRESSID " +
                "WHERE LOWER(a.ADDRESSCITY) = LOWER(:city)",
        nativeQuery = true
    )
    Long countSuppliersByCityNative(@Param("city") String city);

    // Analytics: top suppliers by raw material count
    @Query("SELECT s.suppliersName, COUNT(rm) " +
           "FROM Supplier s JOIN RawMaterial rm ON rm.supplier = s " +
           "GROUP BY s.suppliersName " +
           "ORDER BY COUNT(rm) DESC")
    List<Object[]> getTopSuppliersByMaterialCount();

    @Query("SELECT COUNT(s) FROM Supplier s WHERE UPPER(s.suppliersName) = UPPER(:name)")
    int countByName(@Param("name") String name);

}
