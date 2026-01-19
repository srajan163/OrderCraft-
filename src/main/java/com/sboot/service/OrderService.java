package com.sboot.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sboot.entity.Order;
import com.sboot.repository.OrderRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Call this when procurement officer creates order
    public Order createOrder() {
        Order order = new Order("Order Placed", LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    @Scheduled(fixedRate = 60000)  // Runs every 60 seconds for testing
    public void updateOrderStatuses() {
        List<Order> orders = orderRepository.findByStatusNot("Order Delivered");

        LocalDateTime now = LocalDateTime.now();

        for (Order order : orders) {
            Duration duration = Duration.between(order.getCreatedAt(), now);
            long minutesPassed = duration.toMinutes();

            String newStatus = null;

            if (minutesPassed >= 7) {
                newStatus = "Order Delivered";
            } else if (minutesPassed >= 2) {
                newStatus = "Order Shipped";
            } else if (minutesPassed >= 1) {
                newStatus = "Order Created";
            } else {
                newStatus = "Order Placed";
            }

            if (!order.getStatus().equals(newStatus)) {
                order.setStatus(newStatus);
                orderRepository.save(order);
                System.out.println("Updated order id " + order.getId() + " to " + newStatus);
            }
        }
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
    //Service Done. 

}
