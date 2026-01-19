package com.sboot.entity;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "SUPPLIER_ADDRESS")
public class Supplier_Address {
 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "SUPPLIER_ADDRESS_SEQ", allocationSize = 1)
    @Column(name = "ADDRESSID")
    private Long addressId;
 
    @Column(name = "ADDRESSCITY")
    private String addressCity;
 
    @Column(name = "ADDRESSCOUNTRY")
    private String addressCountry;
 
    @Column(name = "ADDRESSPOSTALCODE")
    private String addressPostalCode;
 
    @Column(name = "ADDRESSSTATE")
    private String addressState;
 
    @Column(name = "ADDRESSSTREET")
    private String addressStreet;
 
    // Getters & Setters
    public Long getAddressId() {
        return addressId;
    }
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
 
    public String getAddressCity() {
        return addressCity;
    }
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }
 
    public String getAddressCountry() {
        return addressCountry;
    }
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }
 
    public String getAddressPostalCode() {
        return addressPostalCode;
    }
    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }
 
    public String getAddressState() {
        return addressState;
    }
    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }
 
    public String getAddressStreet() {
        return addressStreet;
    }
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }
}
 
 