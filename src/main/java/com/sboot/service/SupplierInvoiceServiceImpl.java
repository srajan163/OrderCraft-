package com.sboot.service;
 
import java.util.List;

import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import com.sboot.dto.SupplierInvoiceResponseDTO;

import com.sboot.entity.PurchaseOrder;

import com.sboot.entity.Supplier;

import com.sboot.repository.PurchaseOrdersRepository;
 
@Service

public class SupplierInvoiceServiceImpl {
 
    @Autowired

    private PurchaseOrdersRepository purchaseOrdersRepository;
 
    public List<SupplierInvoiceResponseDTO> generateSupplierInvoices(Long purchaseOrderId) {

        PurchaseOrder order = purchaseOrdersRepository.findById(purchaseOrderId)

                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
 
        Supplier supplier = order.getSupplier();

        if (supplier == null) {

            throw new RuntimeException("No supplier associated with this purchase order.");

        }
 
        return order.getItems().stream()

                .map(item -> {

                    SupplierInvoiceResponseDTO dto = new SupplierInvoiceResponseDTO();

                    dto.setPurchaseOrderId(order.getPoId());

                    dto.setSupplierName(supplier.getSuppliersName());

                    dto.setSupplierEmail(supplier.getSuppliersEmail());

                    dto.setSupplierContact(supplier.getSuppliersPhone());

                    //dto.setSupplierAddress(supplier.getSuppliersAddress());

                    dto.setMaterialName(item.getRawMaterial().getRwName());

                    dto.setQuantity(item.getPoiQuantity());

                    dto.setUnitPrice(item.getPoiCost());

                    dto.setTotal(item.getPoiQuantity() * item.getPoiCost());

                    return dto;

                })

                .collect(Collectors.toList());

    }

}

 