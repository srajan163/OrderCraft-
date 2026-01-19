package com.sboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sboot.entity.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {}
