package com.sboot.service;

import com.sboot.entity.SupplierPayment;
import com.sboot.repository.SupplierPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierHistoryService {

    private final SupplierPaymentRepository supplierPaymentRepository;

    public SupplierHistoryService(SupplierPaymentRepository supplierPaymentRepository) {
        this.supplierPaymentRepository = supplierPaymentRepository;
    }

    public List<SupplierPayment> getSupplierPaymentHistory(Long supplierId) {
        return supplierPaymentRepository.findBySupplier_SuppliersId(supplierId);
    }
}
