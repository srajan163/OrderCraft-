package com.sboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sboot.entity.RawMaterial;

@Repository
public interface RawMaterialsRepository extends JpaRepository<RawMaterial, Long> {}
