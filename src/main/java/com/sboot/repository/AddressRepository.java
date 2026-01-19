package com.sboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.sboot.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}

