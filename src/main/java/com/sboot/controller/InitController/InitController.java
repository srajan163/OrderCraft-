package com.sboot.controller.InitController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sboot.entity.Address;
import com.sboot.entity.Role;
import com.sboot.entity.User;
import com.sboot.repository.AddressRepository;
import com.sboot.repository.RoleRepository;
import com.sboot.repository.UserRepository;

@RestController
@RequestMapping("/setup")
public class InitController {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/init")
    public ResponseEntity<String> initializeData() {
        // Predefined Roles
        Role adminRole = new Role(1L, "ADMIN");
        Role procurementOfficerRole = new Role(2L, "PROCUREMENT_OFFICER");
        Role inventoryManagerRole=new Role(3L,"INVENTORY_MANAGER");
        Role productionManagerRole = new Role(4L, "PRODCUTION_MANAGER");
        Role prodcutionSchedulerRole=new Role(5L,"PRODCUTION_SCHEDULER");
        Role supplierRole=new Role(6L,"SUPPLIER");
        roleRepo.saveAll(List.of(adminRole,  procurementOfficerRole, productionManagerRole,inventoryManagerRole,productionManagerRole,prodcutionSchedulerRole,supplierRole));

        // Address for sample user
        Address address = new Address(
            1L, "123 Main St", "NY", "New York", "10001", "USA"
        );
        addressRepo.save(address);

        // Admin user with encrypted password
        User admin = new User();
        admin.setUserId("1HYD");
        admin.setUserName("admin");
        admin.setUserPassword(passwordEncoder.encode("admin123")); // 🔐 Hashed password
        admin.setUserEmail("admin@example.com");
        admin.setUserFullName("Admin User");
        admin.setUserMobile("9876543210");
        admin.setUserProfileImg("https://example.com/admin.jpg");
        admin.setRole(adminRole);
        admin.setAddress(address);
        userRepo.save(admin);

        return ResponseEntity.ok("Initial roles, address, and admin user inserted successfully.");
    }
}
