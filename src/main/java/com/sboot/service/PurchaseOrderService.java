package com.sboot.service;

import com.sboot.dto.*;
import com.sboot.entity.*;
import com.sboot.repository.*;
import com.sboot.service.MailService.MailService;
import com.sboot.util.InvoicePdfGenerator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PurchaseOrderService {

    @Autowired private PurchaseOrdersRepository purchaseOrderRepository;
    @Autowired private PurchaseOrderItemsRepository purchaseOrderItemRepository;
    @Autowired private SuppliersRepository supplierRepository;
    @Autowired private ProductsRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RawMaterialsRepository rawMaterialsRepository;
    @Autowired private UserService userService;
    @Autowired private CustomerRepository customerRepository;
    @Autowired
    private MailService emailService;
    @Autowired private InvoiceService invoiceService;

    // -----------------------------------------------------------------------------------
    // Supplier Order
    // -----------------------------------------------------------------------------------
    public ResponseEntity<String> createPurchaseOrder(PurchaseOrderCreateRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        String username = principal.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) userOpt = userService.getByEmail(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found."));

        // Create and save the main PurchaseOrder entity
        PurchaseOrder order = new PurchaseOrder();
        order.setPoOrderDate(Optional.ofNullable(request.getPoOrderDate()).orElse(LocalDateTime.now()));
        order.setPoExpectedDeliveryDate(LocalDate.now().plusDays(7));

        order.setUser(user);
        order.setSupplier(supplier);
        order.setPoDeliveryStatus(Optional.ofNullable(request.getPoDeliveryStatus()).orElse("PENDING"));

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // Loop over all raw materials and create PurchaseOrderItems
        for (PurchaseOrderItemRequest itemReq : request.getItems()) {
            RawMaterial rawMaterial = rawMaterialsRepository.findById(itemReq.getrWId())
                    .orElseThrow(() -> new RuntimeException("Raw Material not found."));

            if (rawMaterial.getRwQuantity() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for: " + rawMaterial.getRwName());
            }

            // Deduct stock
            rawMaterial.setRwQuantity(rawMaterial.getRwQuantity() - itemReq.getQuantity());
            rawMaterialsRepository.save(rawMaterial);

            // ✅ Calculate POI cost from unit price
            Float calculatedCost = rawMaterial.getRwUnitPrice() * itemReq.getQuantity();

            // Create and save each purchase order item
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(savedOrder);
            item.setPoiQuantity(itemReq.getQuantity());
            item.setPoiCost(calculatedCost); // ✅ Set cost from unit price * quantity
            item.setRawMaterial(rawMaterial); // ✅ Link raw material

            purchaseOrderItemRepository.save(item);
        }

        return ResponseEntity.ok("Order placed successfully.");
    }
    
    
 // ✅ Fetch all raw materials
    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialsRepository.findAll();
    }

     
    public ResponseEntity<String> createSupplierOrder(PurchaseOrderSupplierRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        // Fetch user
        String username = principal.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) userOpt = userService.getByEmail(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();

        // Fetch default supplier
        Supplier supplier = supplierRepository.findBySuppliersName("BGSW")
                .orElseThrow(() -> new RuntimeException("Default supplier not found."));

        // Create customer
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerEmail(request.getCustomerEmail());
        customer.setCustomerMobile(request.getCustomerMobile());
        customer.setCustomerAddress(request.getCustomerAddress());
        customerRepository.save(customer);

        // Create order
        PurchaseOrder order = new PurchaseOrder();
        order.setPoOrderDate(LocalDateTime.now());
        order.setPoExpectedDeliveryDate(LocalDate.now().plusDays(7));

        order.setUser(user);
        order.setSupplier(supplier);
        order.setPoDeliveryStatus("PENDING");
        order.setCustomer(customer);

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // Add items to order
        List<PurchaseOrderItem> orderItems = new ArrayList<>();
        for (PurchaseOrderSupplierRequest.Item itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            if (product.getProductsQuantity() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for: " + product.getProductsName());
            }

            product.setProductsQuantity(product.getProductsQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(savedOrder);
            item.setProduct(product);
            item.setPoiQuantity(itemReq.getQuantity());
            item.setPoiCost(product.getProductsUnitPrice());

            orderItems.add(item);
        }

        purchaseOrderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);
        purchaseOrderRepository.save(savedOrder);

        // Send invoice email
        try {
            InvoiceResponseDTO invoice = invoiceService.generateInvoice(savedOrder.getPoId());

            if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
                throw new RuntimeException("Invoice generation failed: No items found.");
            }

            byte[] pdf = InvoicePdfGenerator.generatePdf(invoice);

            

            String paymentUrl = "http://localhost:4200/payments/pay?orderId=" + savedOrder.getPoId();

            String htmlBody = "<html>" +
                    "<body style='font-family:Arial,sans-serif;'>" +
                    "<h2 style='color:#2c3e50;'>Hello " + customer.getCustomerName() + ",</h2>" +
                    "<p>Thank you for your order with <strong>OrderCraft</strong>.</p>" +
                    "<p>Your invoice is attached below.</p>" +
                    "<p><strong>To complete your order, please make a payment using the button below:</strong></p>" +
                    "<a href='" + paymentUrl + "' style='background-color:#28a745;color:#fff;padding:10px 20px;" +
                    "text-decoration:none;border-radius:5px;display:inline-block;margin-top:10px;'>Pay Now</a>" +
                    "<p style='font-size: 12px; color:#888;'>— The OrderCraft Team</p>" +
                    "</body></html>";
            
            String subject = "OrderCraft Invoice - Order " + savedOrder.getPoId();
            String attachmentName = "invoice_order_" + savedOrder.getPoId() + ".pdf";

            

emailService.sendEmailWithAttachmentForPapercut(
    "purchase@ordercraft.com",
    customer.getCustomerEmail(),
    subject,
    htmlBody,
    attachmentName,
    pdf
);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Order saved, but failed to send invoice.");
        }

        return ResponseEntity.ok("Order placed and invoice sent successfully.");
    }


    // -----------------------------------------------------------------------------------
    // Update Existing Order
    // -----------------------------------------------------------------------------------
    @Transactional
    public void updateOrderAndItems(Long orderId, PurchaseOrderUpdateRequest request, String username) {
        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"ORDER PLACED".equalsIgnoreCase(order.getPoDeliveryStatus())) {
            throw new RuntimeException("Only PENDING orders can be updated.");
        }

        if (request.getNewExpectedDeliveryDate() != null) {
        	order.setPoExpectedDeliveryDate(request.getNewExpectedDeliveryDate());
        }

        if (request.getNewDeliveryStatus() != null) {
            order.setPoDeliveryStatus(request.getNewDeliveryStatus());
        }

        if (request.getItems() != null) {
            for (PurchaseOrderUpdateRequest.ItemUpdateRequest itemUpdate : request.getItems()) {
                PurchaseOrderItem item = purchaseOrderItemRepository.findById(itemUpdate.getItemId())
                        .orElseThrow(() -> new RuntimeException("Item not found: " + itemUpdate.getItemId()));

                Product product = item.getProduct();
                int oldQty = item.getPoiQuantity();
                Integer newQty = itemUpdate.getNewQuantity();

                if (newQty != null && !newQty.equals(oldQty)) {
                    int diff = newQty - oldQty;
                    if (diff > 0 && product.getProductsQuantity() < diff) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getProductsName());
                    }

                    product.setProductsQuantity(product.getProductsQuantity() - diff);
                    productRepository.save(product);
                    item.setPoiQuantity(newQty);
                }

                if (itemUpdate.getNewCost() != null) {
                    item.setPoiCost(itemUpdate.getNewCost());
                }

                purchaseOrderItemRepository.save(item);
            }
        }

        purchaseOrderRepository.save(order);
    }

    // -----------------------------------------------------------------------------------
    // Cancel Order
    // -----------------------------------------------------------------------------------
    
    @Transactional
    public boolean cancelOrder(Long orderId, Principal principal) {
        Optional<PurchaseOrder> optionalOrder = purchaseOrderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) return false;

        PurchaseOrder order = optionalOrder.get();

        String status = order.getPoDeliveryStatus();
        if (!"ORDER PLACED".equalsIgnoreCase(status) && !"PENDING".equalsIgnoreCase(status)) {
            throw new RuntimeException("Only PENDING or ORDER PLACED orders can be cancelled.");
        }

        List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrder(order);
        for (PurchaseOrderItem item : items) {
            Product product = item.getProduct();
            product.setProductsQuantity(product.getProductsQuantity() + item.getPoiQuantity());
            productRepository.save(product);
        }

        order.setPoDeliveryStatus("CANCELLED");
        purchaseOrderRepository.save(order);
        purchaseOrderRepository.flush(); // <-- force flush (optional)
        
        return true;
    }


    // -----------------------------------------------------------------------------------
    // Utilities
    // -----------------------------------------------------------------------------------
    public PurchaseOrder save(PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }

    private Long generateId() {
        return System.currentTimeMillis();
    }

    private Long generateItemId() {
        return System.nanoTime();
    }
    

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductDTO(
                        p.getProductsId(),
                        p.getProductsName(),
                        p.getProductsUnitPrice(),
                        p.getCategory() != null ? p.getCategory().getCategoryName() : null,        // assuming you have this field in entity
                        p.getProductsQuantity()         // assuming you have this field in entity
                ))
                .collect(Collectors.toList());
    }
    
    public Map<String, Long> getAdminStats() {
        Map<String, Long> stats = new HashMap<>();
 
        stats.put("procurementOfficer", userRepository.countProcurementOfficer());
        stats.put("inventoryManager", userRepository.countInventoryManager());
        stats.put("productionManager", userRepository.countProductionManager());
 
        return stats;
    }
    

//-------------------------Track Order---------------------------------------------
    
    public PurchaseOrderService(PurchaseOrdersRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }
 
    // When procurement officer creates order
    public PurchaseOrder createOrder() {
        PurchaseOrder order = new PurchaseOrder();
        order.setPoDeliveryStatus("ORDER CREATED");  // Initial status
        order.setPoOrderDate(LocalDateTime.now());
        return purchaseOrderRepository.save(order);
    }
 
    // Runs every minute to update order status

    
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateOrderStatuses() {
        // Fetch orders that are NOT delivered and NOT cancelled
        List<PurchaseOrder> orders = purchaseOrderRepository.findByPoDeliveryStatusNotIn(List.of("ORDER DELIVERED", "CANCELLED"));

        LocalDateTime now = LocalDateTime.now();

        for (PurchaseOrder order : orders) {
            LocalDateTime orderDate = order.getPoOrderDate();
            if (orderDate == null) continue;

            Duration duration = Duration.between(orderDate, now);
            long minutes = duration.toMinutes();

            String newStatus;
            if (minutes >= 7) {
                newStatus = "ORDER DELIVERED";
            } else if (minutes >= 2) {
                newStatus = "ORDER SHIPPED";
            } else if (minutes >= 1) {
                newStatus = "ORDER CREATED";
            } else {
                newStatus = "ORDER PLACED";
            }

            if (!order.getPoDeliveryStatus().equalsIgnoreCase(newStatus)) {
                order.setPoDeliveryStatus(newStatus);
                purchaseOrderRepository.save(order);
                System.out.println("Order " + order.getPoId() + " status updated to: " + newStatus);
            }
        }
    }

 
    public PurchaseOrder getOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
 
    
    public Map<String, LocalDateTime> getEffectiveOrderStatusHistory(PurchaseOrder order) {
        LocalDateTime orderDate = order.getPoOrderDate();
        LocalDateTime now = LocalDateTime.now();
        Map<String, LocalDateTime> statusMap = new LinkedHashMap<>();
 
        // "ORDER PLACED" is always effective immediately
        statusMap.put("ORDER PLACED", orderDate);
 
        if (now.isAfter(orderDate.plusMinutes(1)) || now.isEqual(orderDate.plusMinutes(1))) {
            statusMap.put("ORDER CREATED", orderDate.plusMinutes(1));
        }
 
        if (now.isAfter(orderDate.plusMinutes(2)) || now.isEqual(orderDate.plusMinutes(2))) {
            statusMap.put("ORDER SHIPPED", orderDate.plusMinutes(2));
        }
 
        if (now.isAfter(orderDate.plusMinutes(7)) || now.isEqual(orderDate.plusMinutes(7))) {
            statusMap.put("ORDER DELIVERED", orderDate.plusMinutes(7));
        }
 
        return statusMap;
    }
    
  //-------------------------View Orders---------------------------------------------
    public List<PurchaseOrderViewDTO> getOrdersByType(boolean internal, String username) {
        List<PurchaseOrder> allOrders = purchaseOrderRepository.findAll();
 
        // Filter based on order type
        List<PurchaseOrder> filtered = allOrders.stream()
            .filter(order -> {
                String supplierName = order.getSupplier().getSuppliersName();
                boolean isInternal = "BGSW".equalsIgnoreCase(supplierName);
                return internal == isInternal;
            })
            .filter(order -> order.getUser().getUserEmail().equalsIgnoreCase(username)) // Only orders created by the user
            .toList();
 
        return filtered.stream()
            .map(this::mapToDTO)
            .toList();
    }
 
    private PurchaseOrderViewDTO mapToDTO(PurchaseOrder order) {
        PurchaseOrderViewDTO dto = new PurchaseOrderViewDTO();
        dto.setPoId(order.getPoId());
        dto.setSupplierName(order.getSupplier().getSuppliersName());
        dto.setOrderedBy(order.getUser().getUserFullName());
        dto.setPoOrderDate(order.getPoOrderDate());
        dto.setPoExpectedDeliveryDate(order.getPoExpectedDeliveryDate());
        dto.setPoDeliveryStatus(order.getPoDeliveryStatus());
 
        List<PurchaseOrderViewDTO.ItemDTO> itemDTOs = order.getItems().stream().map(item -> {
            PurchaseOrderViewDTO.ItemDTO itemDTO = new PurchaseOrderViewDTO.ItemDTO();
            itemDTO.itemId = item.getPoiId();
            itemDTO.quantity = item.getPoiQuantity();
            itemDTO.cost = item.getPoiCost();
 
            if (item.getProduct() != null) {
                itemDTO.productName = item.getProduct().getProductsName();
            } else if (item.getRawMaterial() != null) {
                itemDTO.productName = item.getRawMaterial().getRwName();
            } else {
                itemDTO.productName = "N/A";
            }
 
            return itemDTO;
        }).toList();
 
        dto.setItems(itemDTOs);
        return dto;
    }
    
    //------------
    public List<PurchaseOrderViewDTO> getOrdersByFilter(Boolean isInternal, String username, String status, LocalDate date, boolean isAdmin) {
        List<PurchaseOrder> orders = purchaseOrderRepository.findAll();
 
        return orders.stream()
            .filter(order -> {
                if (!isAdmin && !order.getUser().getUserEmail().equalsIgnoreCase(username)) return false;
                if (isInternal != null) {
                    boolean isInternalOrder = "BGSW".equalsIgnoreCase(order.getSupplier().getSuppliersName());
                    if (isInternalOrder != isInternal) return false;
                }
                if (status != null && !status.equalsIgnoreCase(order.getPoDeliveryStatus())) return false;
                if (date != null && !date.equals(order.getPoOrderDate().toLocalDate())) return false;
                return true;
            })
            .map(this::mapToDTO)
            .toList();
    }
 // returns small summary + recent orders
    public OrderSummaryDto getLiveOrderSummary() {
        List<PurchaseOrder> orders = purchaseOrderRepository.findAll();

        OrderSummaryDto dto = new OrderSummaryDto();
        dto.setTotalOrders(orders.size());
        long pending = orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getPoDeliveryStatus())).count();
        long shipped = orders.stream().filter(o -> o.getPoDeliveryStatus() != null && o.getPoDeliveryStatus().toUpperCase().contains("SHIPPED")).count();
        long delivered = orders.stream().filter(o -> o.getPoDeliveryStatus() != null && o.getPoDeliveryStatus().toUpperCase().contains("DELIVERED")).count();

        dto.setPendingOrders(pending);
        dto.setShippedOrders(shipped);
        dto.setDeliveredOrders(delivered);

        // recent N (map to SimpleOrderDto)
        int N = 5;
        List<OrderSummaryDto.SimpleOrderDto> recents = orders.stream()
                .sorted((a,b) -> {
                    if (a.getPoOrderDate() == null) return 1;
                    if (b.getPoOrderDate() == null) return -1;
                    return b.getPoOrderDate().compareTo(a.getPoOrderDate());
                })
                .limit(N)
                .map(o -> {
                    OrderSummaryDto.SimpleOrderDto s = new OrderSummaryDto.SimpleOrderDto();
                    s.poId = o.getPoId();
                    s.supplierName = o.getSupplier() != null ? o.getSupplier().getSuppliersName() : null;
                    s.orderedBy = o.getUser() != null ? o.getUser().getUserFullName() : null;
                    s.status = o.getPoDeliveryStatus();
                    s.orderDate = o.getPoOrderDate();
                    return s;
                })
                .toList();

        dto.setRecent(recents);
        return dto;
    }

    
 
 
 
}
