//package com.sboot.controller;
//
//import com.sboot.dto.CustomerOrderRequest;
//import com.sboot.dto.InvoiceResponseDTO;
//import com.sboot.entity.Customer;
//import com.sboot.repository.CustomerRepository;
//import com.sboot.service.MailService.MailService;
//import com.sboot.util.InvoicePdfGenerator;
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.atomic.AtomicLong;
//
//@RestController
//@RequestMapping("/api/order")
//public class CustomerOrderController {
//
//    private final AtomicLong orderIdGenerator = new AtomicLong(1000);
//
//    @Autowired
//    private MailService emailService;
//
//    
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @PostMapping("/customer")
//    public ResponseEntity<byte[]> placeCustomerOrder(@RequestBody CustomerOrderRequest request) {
//        try {
//            // ✅ Check if customer already exists by email
//            Customer customer = customerRepository
//                    .findByCustomerEmail(request.getCustomerEmail())
//                    .orElseGet(() -> {
//                        Customer newCustomer = new Customer();
//                        newCustomer.setCustomerName(request.getCustomerName());
//                        newCustomer.setCustomerEmail(request.getCustomerEmail());
//                        newCustomer.setCustomerMobile(request.getPhoneNumber());
//                        newCustomer.setCustomerAddress(request.getBillingAddress());
//                        return customerRepository.save(newCustomer); // ✅ Only save if not exists
//                    });
//
//            // 🔧 You can now use `customer.getCustomerId()` in further logic
//
//            // Generate invoice etc.
//            InvoiceResponseDTO invoice = new InvoiceResponseDTO();
//            invoice.setOrderId(orderIdGenerator.incrementAndGet());
//            invoice.setCustomerName(customer.getCustomerName());
//            invoice.setCustomerEmail(customer.getCustomerEmail());
//            invoice.setPhoneNumber(customer.getCustomerMobile());
//            invoice.setBillingAddress(customer.getCustomerAddress());
//            invoice.setItems(request.getItems());
//
//            byte[] pdfBytes = InvoicePdfGenerator.generatePdf(invoice);
//
//            try {
//                emailService.sendInvoiceEmail(
//                        customer.getCustomerEmail(),
//                        "Here is invoice for your order to OrderCraft",
//                        "Dear " + customer.getCustomerName() + ",\n\nPlease find attached your invoice.",
//                        pdfBytes
//                );
//            } catch (MessagingException e) {
//                System.err.println("Failed to send email: " + e.getMessage());
//            }
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("inline", "invoice_" + invoice.getOrderId() + ".pdf");
//
//            return ResponseEntity.ok().headers(headers).body(pdfBytes);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(("Failed to process order: " + e.getMessage()).getBytes());
//        }
//    }
//}    