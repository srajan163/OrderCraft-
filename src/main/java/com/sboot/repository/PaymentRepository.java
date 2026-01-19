package com.sboot.repository;

import com.sboot.entity.PurchaseOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PurchaseOrder, Long> {

}

 