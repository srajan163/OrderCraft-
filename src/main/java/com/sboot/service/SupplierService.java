package com.sboot.service;

import com.sboot.entity.Supplier;
import com.sboot.entity.Supplier_Address;
import com.sboot.repository.SupplierAddressRepository;
import com.sboot.repository.SuppliersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SuppliersRepository supplierRepository;

    @Autowired
    private SupplierAddressRepository supplierAddressRepository;

    // ---------------------------
    // ADD NEW SUPPLIER
    // ---------------------------
    public Supplier createSupplier(Supplier supplier) {

        // Check duplicate supplier name
        if (supplierRepository.countByName(supplier.getSuppliersName()) > 0) {
            throw new RuntimeException("Supplier already exists");
        }

        // Address will be saved automatically due to cascade from Supplier entity
        return supplierRepository.save(supplier);
    }

    // ---------------------------
    // UPDATE SUPPLIER
    // ---------------------------
    public Supplier updateSupplier(Long supplierId, Supplier updatedData) {

        Supplier existing = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // Update supplier main fields
        existing.setSuppliersName(updatedData.getSuppliersName());
        existing.setSuppliersPhone(updatedData.getSuppliersPhone());
        existing.setSuppliersEmail(updatedData.getSuppliersEmail());
        existing.setSuppliersContactPerson(updatedData.getSuppliersContactPerson());
        existing.setSuppliersRating(updatedData.getSuppliersRating());

        // ---------------------------
        // UPDATE SUPPLIER ADDRESS
        // ---------------------------
        if (updatedData.getSupplierAddress() != null) {

            Supplier_Address updatedAddress = updatedData.getSupplierAddress();
            Supplier_Address existingAddress = existing.getSupplierAddress();

            if (existingAddress == null) {
                // No previous address → add new one
                existing.setSupplierAddress(updatedAddress);
            } else {
                // Update only fields of existing address
                existingAddress.setAddressStreet(updatedAddress.getAddressStreet());
                existingAddress.setAddressCity(updatedAddress.getAddressCity());
                existingAddress.setAddressState(updatedAddress.getAddressState());
                existingAddress.setAddressPostalCode(updatedAddress.getAddressPostalCode());
                existingAddress.setAddressCountry(updatedAddress.getAddressCountry());
            }
        }

        return supplierRepository.save(existing);
    }

    // ---------------------------
    // DELETE SUPPLIER
    // ---------------------------
    public void deleteSupplier(Long supplierId) {

        Supplier existing = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierRepository.delete(existing);
    }

    // ---------------------------
    // GET ALL SUPPLIERS
    // ---------------------------
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    public long getTotalSuppliers() {
        return supplierRepository.count();
    }
}
