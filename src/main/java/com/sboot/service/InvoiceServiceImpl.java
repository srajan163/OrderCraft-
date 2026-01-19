package com.sboot.service;

import com.sboot.dto.InvoiceResponseDTO;
import com.sboot.dto.OrderItemDTO;
import com.sboot.entity.Customer;
import com.sboot.entity.PurchaseOrder;
import com.sboot.repository.PurchaseOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    @Override
    public InvoiceResponseDTO generateInvoice(Long orderId) {
        // Fetch the order by ID
        PurchaseOrder order = purchaseOrdersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));

        // Get customer from the order
        Customer customer = order.getCustomer();
        if (customer == null) {
            throw new RuntimeException("No customer associated with order ID: " + orderId);
        }

        // Build the item list
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> {
                    String productName = item.getProduct() != null
                            ? item.getProduct().getProductsName()
                            : "Unknown Product";

                    String category = (item.getProduct() != null && item.getProduct().getCategory() != null)
                            ? item.getProduct().getCategory().getCategoryName()
                            : "Unknown Category";

                    return new OrderItemDTO(
                            productName,
                            category,
                            item.getPoiQuantity(),
                            item.getPoiCost()
                    );
                })
                .collect(Collectors.toList());

        // Construct and return the invoice DTO
        return new InvoiceResponseDTO(
                order.getPoId(),
                safe(customer.getCustomerAddress()),
                safe(customer.getCustomerMobile()),
                safe(customer.getCustomerName()),
                safe(customer.getCustomerEmail()),
                items
        );
    }

    // Null-safe utility
    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
