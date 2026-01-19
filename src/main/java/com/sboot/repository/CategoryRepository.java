package com.sboot.repository;
 
import com.sboot.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
 
@Repository

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

 