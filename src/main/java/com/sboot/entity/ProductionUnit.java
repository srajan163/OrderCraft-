package com.sboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTION_UNITS")
public class ProductionUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_seq")
    @SequenceGenerator(name = "unit_seq", sequenceName = "UNIT_SEQ", allocationSize = 1)
    
    @Column(name = "UNITID")
    private Long unitId;

    @Column(name = "UNITNAME", nullable = false)
    private String unitName;

    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @Column(name = "AVAILABLE", nullable = false)
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORIESID")
    @JsonIgnoreProperties({"units", "products"})   // prevent infinite recursion
    private Category category;
    
    @Column(name = "STATUS", nullable = true)
    private String status; // e.g., ON_TRACK, DELAYED, STOPPED

    // Getters & Setters
    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
