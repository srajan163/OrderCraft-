package com.sboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sboot.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusNot(String status);
}

