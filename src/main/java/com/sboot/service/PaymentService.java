package com.sboot.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sboot.entity.PurchaseOrder;

import com.sboot.util.MockPaymentGateway;

import com.sboot.repository.PaymentRepository;

@Service

public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final MockPaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, MockPaymentGateway paymentGateway) {

        this.paymentRepository = paymentRepository;

        this.paymentGateway = paymentGateway;

    }

    public boolean processOrderPayment(Long orderId, String paymentMethod) {

        Optional<PurchaseOrder> optionalOrder = paymentRepository.findById(orderId);

        if (!optionalOrder.isPresent()) {

            throw new RuntimeException("Order not found with ID " + orderId);

        }

        PurchaseOrder order = optionalOrder.get();

        // TODO: Replace 0.0 with actual amount if needed

        boolean paymentSuccess = paymentGateway.chargeOrder(orderId, 0.0);

        if (paymentSuccess) {

            order.setPoPaymentStatus("Paid");

            order.setPoPaymentMethod(paymentMethod); // Set payment method here

            paymentRepository.save(order);

            return true;

        } else {

            return false;

        }

    }

}


 