package com.sboot.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "supplier_ratings", indexes = {
        @Index(name = "idx_supplier_id", columnList = "supplier_id")
})
public class SupplierRating {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_rating_seq")
	    @SequenceGenerator(
	        name = "supplier_rating_seq",
	        sequenceName = "SUPPLIER_RATING_SEQ",
	        allocationSize = 1,
	        initialValue = 1
	    )
    private Long id;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "SUPPLIERSID")
	private Supplier supplier;

//    @Column(name="supplier_id", nullable = false)
//    private Long supplierId;

    @Column(nullable = false)
    private Integer rating; // 1..5

    @Column(length = 2000)
    private String review;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
//    @Column(name = "suppliers_name")
//    private String supplierName;

//    public String getSupplierName() {
//		return supplierName;
//	}
//
//	public void setSupplierName(String supplierName) {
//		this.supplierName = supplierName;
//	}

	public SupplierRating() {}

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

//    public Long getSupplierId() { return supplierId; }
//    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier long1) {
		this.supplier = long1;
	}

	public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupplierRating that = (SupplierRating) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
