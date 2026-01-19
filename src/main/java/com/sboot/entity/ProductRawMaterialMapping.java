package com.sboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_RAWMATERIAL_MAPPING")
public class ProductRawMaterialMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapping_seq")
    @SequenceGenerator(name = "mapping_seq", sequenceName = "MAPPING_SEQ", allocationSize = 1)
    @Column(name = "MAPPING_ID")
    private Long mappingId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCTSID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "RAWMATERIAL_ID", referencedColumnName = "RWID")
    private RawMaterial rawMaterial;

    @Column(name = "QUANTITY_REQUIRED", nullable = false)
    private Integer quantityRequired;

    // Getters & setters
    public Long getMappingId() {
        return mappingId;
    }
    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }
    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }
    public Integer getQuantityRequired() {
        return quantityRequired;
    }
    public void setQuantityRequired(Integer quantityRequired) {
        this.quantityRequired = quantityRequired;
    }
}
