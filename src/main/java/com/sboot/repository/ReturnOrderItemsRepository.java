package com.sboot.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 
import com.sboot.entity.ReturnOrderItem;
 
@Repository

public interface ReturnOrderItemsRepository extends JpaRepository<ReturnOrderItem, Long> {}

 