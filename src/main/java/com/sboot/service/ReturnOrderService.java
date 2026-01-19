package com.sboot.service;
 
import com.sboot.dto.ApiResponseDTO;

import com.sboot.dto.ReturnOrderItemDTO;

import com.sboot.dto.ReturnOrderRequestDTO;

import com.sboot.entity.*;

import com.sboot.repository.*;
import com.sboot.service.MailService.MailService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
 
import java.util.ArrayList;

import java.util.Date;

import java.util.List;
 
@Service

public class ReturnOrderService {
 
    @Autowired private ReturnOrdersRepository returnOrderRepository;

    @Autowired private ReturnOrderItemsRepository returnOrderItemRepository;

    @Autowired private PurchaseOrdersRepository purchaseOrderRepository;

    @Autowired private ProductsRepository productRepository;

    @Autowired private UserRepository userRepository;
    
    @Autowired private PurchaseOrderItemsRepository purchaseOrderItemRepository;
    
    @Autowired private MailService emailService;
 
    /**

     * Creates a return order.  

     * Always returns ResponseEntity&lt;ApiResponseDTO&gt; so the controller

     * can directly forward it.

     */

    @Transactional
    public ResponseEntity<ApiResponseDTO> createReturnOrder(ReturnOrderRequestDTO dto) {
 
        User returnedBy = userRepository.findById(dto.getReturnedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getReturnedByUserId()));
 
        // Create main ReturnOrder
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setRoReturnDate(new Date());
        returnOrder.setRoReturnReason(dto.getReturnReason());
        returnOrder.setRoStatus(dto.getStatus());
        returnOrder.setReturnedBy(returnedBy);
 
        List<ReturnOrderItem> returnItems = new ArrayList<>();
        PurchaseOrder purchaseOrder = null;
        Customer customer = null;
 
        for (ReturnOrderItemDTO itemDto : dto.getItems()) {
            // 1. Fetch PurchaseOrderItem
            PurchaseOrderItem poi = purchaseOrderItemRepository.findById(itemDto.getPurchaseOrderItemId())
                    .orElseThrow(() -> new RuntimeException("Purchase Order Item not found: " + itemDto.getPurchaseOrderItemId()));
 
            // 2. Validate quantity
            if (poi.getPoiQuantity() < itemDto.getQuantity()) {
                ApiResponseDTO body = new ApiResponseDTO(
                        "Quantity exceeds the purchased number",
                        null);
                return ResponseEntity.ok(body);
            }
            
            if(poi.isReturned()) {
            	ApiResponseDTO body = new ApiResponseDTO(
                        "This order has already been returned and cannot be returned again.",
                        null);
                return ResponseEntity.ok(body);
            	
            }
 
            // 3. Mark item as returned
            poi.setReturned(true);
            purchaseOrderItemRepository.save(poi);
 
            // 4. Prepare ReturnOrderItem
            ReturnOrderItem roi = new ReturnOrderItem();
            roi.setReturnOrder(returnOrder);
            roi.setProduct(poi.getProduct());
            roi.setReturnQuantity(itemDto.getQuantity());
            roi.setConditionNote(itemDto.getConditionNote());
            returnItems.add(roi);
 
            // Set purchaseOrder and customer only once (all items should belong to the same order)
            if (purchaseOrder == null) {
                purchaseOrder = poi.getPurchaseOrder();
                customer = purchaseOrder.getCustomer();
            }
        }
 
        returnOrder.setItems(returnItems);
        returnOrder.setPurchaseOrder(purchaseOrder);
        ReturnOrder savedOrder = returnOrderRepository.save(returnOrder);
        returnOrderItemRepository.saveAll(returnItems);
 
        // Send confirmation email
        if (customer != null && customer.getCustomerEmail() != null) {
            try {
                emailService.sendEmail(
                    customer.getCustomerEmail(),
                    "Return Confirmation - PO " + purchaseOrder.getPoId(),
                    String.format("""
                        Hi %s,
 
                        Your return for order #%d was successfully submitted.
                        Reason: %s
                        Date: %s
 
                        Thank you,
                        OrderCraft
                        """,
                        customer.getCustomerName(),
                        purchaseOrder.getPoId(),
                        dto.getReturnReason(),
                        new Date()
                    )
                );
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
            }
        }
 
        String responseMessage = String.format("Return order submitted successfully for customer: %s",
                customer != null ? customer.getCustomerName() : "N/A");
 
        ApiResponseDTO body = new ApiResponseDTO(responseMessage, savedOrder.getRoId());
        return ResponseEntity.ok(body);
    }
 
} 