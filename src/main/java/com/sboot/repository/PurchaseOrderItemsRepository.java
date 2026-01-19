package com.sboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sboot.entity.PurchaseOrder;
import com.sboot.entity.PurchaseOrderItem;

@Repository
public interface PurchaseOrderItemsRepository extends JpaRepository<PurchaseOrderItem, Long> {
	
	List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder order);

}
