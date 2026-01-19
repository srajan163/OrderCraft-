package com.sboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SUPPLIERS")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suppliers_seq")
    @SequenceGenerator(name = "suppliers_seq", sequenceName = "SUPPLIERS_SEQ", allocationSize = 1)
    @Column(name = "SUPPLIERSID")
    private Long suppliersId;

    @Column(name = "SUPPLIERSNAME")
    private String suppliersName;

    @Column(name = "SUPPLIERSPHONE")
    private String suppliersPhone;

    @Column(name = "SUPPLIERSEMAIL")
    private String suppliersEmail;

    @Column(name = "SUPPLIERSCONTACTPERSON")
    private String suppliersContactPerson;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SUPPLIERSADDRESSID", referencedColumnName = "ADDRESSID")
    private Supplier_Address supplierAddress;

    @Column(name = "SUPPLIERSRATING")
    private Double suppliersRating;

    // Getters & Setters

    public Long getSuppliersId() {
        return suppliersId;
    }
    public void setSuppliersId(Long suppliersId) {
        this.suppliersId = suppliersId;
    }

    public String getSuppliersName() {
        return suppliersName;
    }
    public void setSuppliersName(String suppliersName) {
        this.suppliersName = suppliersName;
    }

    public String getSuppliersPhone() {
        return suppliersPhone;
    }
    public void setSuppliersPhone(String suppliersPhone) {
        this.suppliersPhone = suppliersPhone;
    }

    public String getSuppliersEmail() {
        return suppliersEmail;
    }
    public void setSuppliersEmail(String suppliersEmail) {
        this.suppliersEmail = suppliersEmail;
    }

    public String getSuppliersContactPerson() {
        return suppliersContactPerson;
    }
    public void setSuppliersContactPerson(String suppliersContactPerson) {
        this.suppliersContactPerson = suppliersContactPerson;
    }

    public Supplier_Address getSupplierAddress() {
        return supplierAddress;
    }
    public void setSupplierAddress(Supplier_Address supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public Double getSuppliersRating() {
        return suppliersRating;
    }
    public void setSuppliersRating(Double suppliersRating) {
        this.suppliersRating = suppliersRating;
    }
}
