package com.sboot.controller;

import org.springframework.web.bind.annotation.*;

import com.sboot.entity.Order;
import com.sboot.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Order createOrder() {
        return orderService.createOrder();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    
    @GetMapping("/{id}/status")
    public String getOrderStatus(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return "Order status: " + order.getStatus();
    }

}
