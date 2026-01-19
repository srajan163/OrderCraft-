package com.sboot.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class Product {

	@Id
	
	@SequenceGenerator(name = "products_seq", sequenceName = "PRODUCTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
	
	@Column(name = "PRODUCTSID")
	private Long productsId;

    @Column(name = "PRODUCTSNAME")
    private String productsName;

    @Column(name = "PRODUCTSDESCRIPTION")

    private String productsDescription;

    @Column(name = "PRODUCTSUNITPRICE")
    private Float productsUnitPrice;

    @Column(name = "PRODUCTSQUANTITY")
    private Integer productsQuantity;

    @Column(name = "PRODUCTSIMAGE")
    private String productsImage;

    @Column(name = "MAXTHRESHOLD")
    private Integer maxThreshold;

    @Column(name = "MINTHRESHOLD")
    private Integer minThreshold;

    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "PRODUCTSCATEGORYID", referencedColumnName = "CATEGORIESID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "PRODUCTSSUPPLIERID", referencedColumnName = "SUPPLIERSID")
    private Supplier supplier;

    // ---------- Getters & Setters ----------

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getProductsDescription() {
        return productsDescription;
    }

    public void setProductsDescription(String productsDescription) {
        this.productsDescription = productsDescription;
    }

    public Float getProductsUnitPrice() {
        return productsUnitPrice;
    }

    public void setProductsUnitPrice(Float productsUnitPrice) {
        this.productsUnitPrice = productsUnitPrice;
    }

    public Integer getProductsQuantity() {
        return productsQuantity;
    }

    public void setProductsQuantity(Integer productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    public String getProductsImage() {
        return productsImage;
    }

    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    public Integer getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(Integer maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public Integer getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
