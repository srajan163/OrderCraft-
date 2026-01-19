package com.sboot.scheduler;  // Use your base package

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import com.sboot.entity.Order;
import com.sboot.repository.OrderRepository;  // Your repository package

@Component
public class OrderStatusScheduler {

    private final OrderRepository repository;

    public OrderStatusScheduler(OrderRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void updateStatuses() {
        LocalDateTime now = LocalDateTime.now();

        var ordersToUpdate = repository.findAll().stream()
            .filter(order -> order.getStatus().equals("Processing") &&
            		order.getCreatedAt().isBefore(now.minusMinutes(1))
)
            .toList();

        for (Order order : ordersToUpdate) {
            order.setStatus("Completed");
            repository.save(order);
            System.out.println("Updated order id " + order.getId() + " to Completed");
        }
    }
}

