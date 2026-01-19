package com.sboot.service;

import org.springframework.stereotype.Service;
import com.sboot.repository.UserRepository;
import com.sboot.repository.SuppliersRepository;

/**
 * Simple service returning live counts from DB.
 */
@Service
public class StatsService {

    private final UserRepository userRepo;
    private final SuppliersRepository supplierRepo;

    public StatsService(UserRepository userRepo, SuppliersRepository supplierRepo) {
        this.userRepo = userRepo;
        this.supplierRepo = supplierRepo;
    }

    public long getUserCount() {
        return userRepo.count();
    }

    public long getSupplierCount() {
        return supplierRepo.count();
    }

    public StatsOverview getOverview() {
        return new StatsOverview(getUserCount(), getSupplierCount());
    }

    // Plain DTO (no record in case your project targets older Java)
    public static class StatsOverview {
        private long users;
        private long suppliers;

        public StatsOverview() {}
        public StatsOverview(long users, long suppliers) {
            this.users = users;
            this.suppliers = suppliers;
        }
        public long getUsers() { return users; }
        public void setUsers(long users) { this.users = users; }
        public long getSuppliers() { return suppliers; }
        public void setSuppliers(long suppliers) { this.suppliers = suppliers; }
    }
    // Streams added .
}
