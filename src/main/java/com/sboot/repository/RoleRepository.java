package com.sboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.sboot.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {}
