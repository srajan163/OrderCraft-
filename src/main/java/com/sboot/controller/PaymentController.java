package com.sboot.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.sboot.service.PaymentService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/orders")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {

        this.paymentService = paymentService;

    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payForOrder(

            @PathVariable("id") Long orderId,

            @RequestBody String paymentMethod  // Receive payment method directly

    ) {

        try {

            boolean paymentSuccessful = paymentService.processOrderPayment(orderId, paymentMethod);

            if (paymentSuccessful) {

                return ResponseEntity.ok("Payment successful");

            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");

            }

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)

                    .body("Payment gateway unavailable or internal error");

        }

    }

}

