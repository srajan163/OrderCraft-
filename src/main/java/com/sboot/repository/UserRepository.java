package com.sboot.repository;

import com.sboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

	  


	  @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleName = 'PROCUREMENT_OFFICER'")
	    Long countProcurementOfficer();


    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleName = 'INVENTORY_MANAGER'")


	    Long countInventoryManager();

	    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleName = 'PRODUCTION_MANAGER'")

	    Long countProductionManager();


    // ✅ Find user by email
    @Query("SELECT u FROM User u WHERE u.userEmail = :email")
    Optional<User> findByEmail(@Param("email") String email);

    // ✅ Check if user exists by email
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userEmail = :email")
    boolean existsByUserEmail(@Param("email") String email);

    // ✅ Find user by username
    Optional<User> findByUserName(String username);


    List<User> findByAccountLockedAtIsNotNull();
}
