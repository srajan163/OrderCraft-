package com.sboot.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 
import com.sboot.entity.ReturnOrder;
 
@Repository

public interface ReturnOrdersRepository extends JpaRepository<ReturnOrder, Long> {}

 