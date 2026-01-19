package com.sboot.entity;

 
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDate;

import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "PRODUCTION_SCHEDULE")
public class ProductionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ps_seq")
    @SequenceGenerator(
            name = "ps_seq",
            sequenceName = "PRODUCTION_SCHEDULE_SEQ",
            allocationSize = 1
    )
    @Column(name = "PSID")   // keep this if column still exists as PK
    private Long psId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCTSID" )
    private Product product;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "UNIT_ID", referencedColumnName = "UNITID")
    private ProductionUnit productionUnit;

    @Column(name = "PS_START_DATE", nullable = false)
    private LocalDate psStartDate;

    @Column(name = "PS_END_DATE", nullable = false)
    private LocalDate psEndDate;

    @Column(name = "PS_QUANTITY", nullable = false)
    private Integer psQuantity;

    @Column(name = "STATUS", length = 50)
    private String status;
    
    @Column(name = "PS_DEADLINE", nullable = false, columnDefinition = "DATE DEFAULT SYSDATE")
    private LocalDate psDeadline;

 
    public LocalDate getPsDeadline() {
		return psDeadline;
	}

	public void setPsDeadline(LocalDate psDeadline) {
		this.psDeadline = psDeadline;
	}

    // Getters and Setters
    public Long getPsId() {
        return psId;
    }

    public void setPsId(Long psId) {
        this.psId = psId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductionUnit getProductionUnit() {
        return productionUnit;
    }

    public void setProductionUnit(ProductionUnit productionUnit) {
        this.productionUnit = productionUnit;
    }

    public LocalDate getPsStartDate() {
        return psStartDate;
    }

    public void setPsStartDate(LocalDate psStartDate) {
        this.psStartDate = psStartDate;
    }

    public LocalDate getPsEndDate() {
        return psEndDate;
    }

    public void setPsEndDate(LocalDate psEndDate) {
        this.psEndDate = psEndDate;
    }

    public Integer getPsQuantity() {
        return psQuantity;
    }

    public void setPsQuantity(Integer psQuantity) {
        this.psQuantity = psQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

