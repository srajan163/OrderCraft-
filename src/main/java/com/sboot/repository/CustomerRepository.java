package com.sboot.repository;

import com.sboot.entity.Customer;
import com.sboot.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerEmail(String email); // Optional duplicate check

	Optional<User> findByCustomerMobile(String customerMobile);
}
