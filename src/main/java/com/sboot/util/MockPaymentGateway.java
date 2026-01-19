package com.sboot.util;

import java.util.Random;
import org.springframework.stereotype.Component;
@Component
public class MockPaymentGateway {
    public boolean chargeOrder(Long orderId, double amount) {
        return new Random().nextDouble() < 0.9; // 90% success rate
    }
}