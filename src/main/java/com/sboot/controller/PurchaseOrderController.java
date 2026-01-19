package com.sboot.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sboot.dto.*;
import com.sboot.entity.*;
import com.sboot.repository.*;
import com.sboot.service.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RawMaterialsRepository rawMaterialsRepository;
    
    @Autowired
    private InvoiceServiceImpl invoiceServiceImpl;

    @Autowired
    private SuppliersRepository supplierRepository;

    @Autowired
    private ProductsRepository productRepository;
    
    @Autowired
    private SupplierInvoiceService supplierInvoiceService;
    
    @Autowired 
    private ReturnOrderService  returnOrderService;
    
    

    // ------------------------- PRODUCTS -------------------------

    
//    @GetMapping("/products")
//    public List<ProductDTO> getAllProducts() {
//    return purchaseOrderService.getAllProducts();
//        return productRepository.findAll().stream()
//                .map(p -> new ProductDTO(p.getProductsId(), p.getProductsName(), p.getProductsUnitPrice(), p.getProductsQuantity()))
//                .toList();
//
//    }
    
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductDTO(p.getProductsId(), p.getProductsName(), p.getProductsUnitPrice(), p.getProductsQuantity()))
                .toList();
    }

    
    @GetMapping("/stats")
    public Map<String, Long> getAdminStats() {
        return purchaseOrderService.getAdminStats();
    }


    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String type,
            @RequestParam String query) {

        List<Product> products;

        try {
            if ("id".equalsIgnoreCase(type)) {
                Long id = Long.parseLong(query);
                products = productRepository.findByProductsId(id);
            } else {
                products = productRepository.searchByProductsNameContainingIgnoreCase(query);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(products);
    }

    // ------------------------- CUSTOMER -------------------------
    @GetMapping("/{customerId}")
    public ResponseEntity<User> getCustomerDetails(@PathVariable String customerId) {
        return userRepository.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}/address")
    public ResponseEntity<User> updateCustomerAddress(
            @PathVariable String customerId,
            @RequestBody Map<String, Address> body) {

        return userRepository.findById(customerId)
                .map(customer -> {
                    customer.setAddress(body.get("address"));
                    return ResponseEntity.ok(userRepository.save(customer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------------- ORDER MANAGEMENT -------------------------
    @PostMapping("/purchase-orders")
    public ResponseEntity<String> createOrder(@RequestBody PurchaseOrderCreateRequest request, Principal principal) {
        purchaseOrderService.createPurchaseOrder(request, principal);
        return ResponseEntity.ok("Purchase Order created successfully");
    }
    
    

//    @PostMapping("/internal-order")
//    public ResponseEntity<String> createSupplierOrder(@RequestBody PurchaseOrderSupplierRequest request, Principal principal) {
//        return purchaseOrderService.createSupplierOrder(request, principal);
//    }    
    
    //Controller changed  20/08
    
    @PostMapping("/internal-order")
    public ResponseEntity<String> createSupplierOrder(
            @RequestBody PurchaseOrderSupplierRequest request,
            Principal principal) {

        if (principal == null) {
            System.out.println("Principal is null (no user authenticated)");
        } else {
            System.out.println("Authenticated user: " + principal.getName());
        }

        return purchaseOrderService.createSupplierOrder(request, principal);
    }
    
    //Get update orders
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long orderId) {
        PurchaseOrder order = purchaseOrderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


     //Update orders 
 
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable Long orderId,
            @RequestBody PurchaseOrderUpdateRequest request,
            Principal principal) {
        purchaseOrderService.updateOrderAndItems(orderId, request, principal.getName());
        return ResponseEntity.ok("Order updated successfully");
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId, Principal principal) {
        boolean cancelled = purchaseOrderService.cancelOrder(orderId, principal);
        if (cancelled) {
            return ResponseEntity.ok("Order cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot cancel the order.");
        }
    }
    
    

    // Supplier order pdf
 // In PurchaseOrderController.java
    

    @GetMapping("/raw-materials")
    public ResponseEntity<List<RawMaterial>> getAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialsRepository.findAll();
        return ResponseEntity.ok(rawMaterials);
    }

    // ✅ GET single raw material by ID
    @GetMapping("/raw-materials/{id}")
    public ResponseEntity<RawMaterial> getRawMaterialById(@PathVariable Long id) {
        return rawMaterialsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 

    // ------------------------- SUPPLIERS -------------------------
    @GetMapping("/suppliers")
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(s -> new SupplierDTO(s.getSuppliersId(), s.getSuppliersName()))
                .toList();
    }

    // ------------------------- INVOICE PDF DOWNLOAD -------------------------
    @GetMapping("/purchase-order/{id}")
    public ResponseEntity<InvoiceResponseDTO> getPurchaseOrderInvoice(@PathVariable String id) {
        // Sanitize input
        String cleanId = id.replaceAll("[\\r\\n]", "").trim();

        Long orderId;
        try {
            orderId = Long.parseLong(cleanId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }

        PurchaseOrder order = purchaseOrdersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        InvoiceResponseDTO pdf = invoiceServiceImpl.generateInvoice(orderId); // Assumes this returns PDF byte[]

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_PO_" + order.getPoId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    //___________________________Return Order_____________________
    @PostMapping("/return-orders")
    public ResponseEntity<ApiResponseDTO> submitReturnOrder(
            @RequestBody ReturnOrderRequestDTO dto) {
        return returnOrderService.createReturnOrder(dto);
    }
    
    @GetMapping("/purchase-order/{poId}/items")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrderWithItems(@PathVariable Long poId) {
        PurchaseOrder purchaseOrder = purchaseOrdersRepository.findById(poId)
            .orElseThrow(() -> new RuntimeException("Purchase order not found: " + poId));

        // Map items to DTO
        List<PurchaseOrderItemDTO> items = purchaseOrder.getItems().stream()
            .filter(item -> !item.isReturned()) // exclude returned items
            .filter(item -> item.getProduct() != null)
            .map(item -> new PurchaseOrderItemDTO(
                    item.getPoiId(),
                    item.getProduct().getProductsId(),
                    item.getProduct().getProductsName(),
                    item.getPoiQuantity()
            ))
            .toList();

        // Create a wrapper DTO including userId
        PurchaseOrderDTO dto = new PurchaseOrderDTO(
            purchaseOrder.getPoId(),
            purchaseOrder.getUser().getUserId(), // String userId
            items
        );

        return ResponseEntity.ok(dto);
    }


    
    //---------------------------------Track Order-------------------------------------------------
    
    @GetMapping("/{id}")
    public PurchaseOrder getOrder(@PathVariable Long id) {
        return purchaseOrderService.getOrderById(id);
    }
 
    @GetMapping("/{id}/status")
    public String getOrderStatus(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.getOrderById(id);
        return "Current Status: " + order.getPoDeliveryStatus();
    }
 
    @GetMapping("/{id}/status/history")
    public Map<String, LocalDateTime> getOrderStatusHistory(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.getOrderById(id);
        return purchaseOrderService.getEffectiveOrderStatusHistory(order);
    }
  //----------------------view order--------------  
 // Procurement officer - their supplier orders
    
    @GetMapping("/orders/supplier")
    public ResponseEntity<List<PurchaseOrderViewDTO>> getMySupplierOrders(
            Principal principal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(purchaseOrderService.getOrdersByFilter(false, principal.getName(), status, date, false));
    }

    // Procurement officer - their internal orders
    @GetMapping("/orders/internal")
    public ResponseEntity<List<PurchaseOrderViewDTO>> getMyInternalOrders(
            Principal principal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(purchaseOrderService.getOrdersByFilter(true, principal.getName(), status, date, false));
    }

    // Admin - all orders
    @GetMapping("/admin/orders")
    public ResponseEntity<List<PurchaseOrderViewDTO>> getAllOrders(
            @RequestParam(required = false) Boolean internal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(purchaseOrderService.getOrdersByFilter(internal, null, status, date, true));
    }


}
