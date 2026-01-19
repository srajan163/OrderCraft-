package com.sboot.service;

import com.sboot.dto.ProductionScheduleCreateRequest;

import com.sboot.dto.ProductionScheduleResponse;

import com.sboot.entity.InventoryTransaction;

import com.sboot.entity.Product;

import com.sboot.entity.ProductionSchedule;

import com.sboot.repository.InventoryTransactionRepository;

import com.sboot.repository.ProductsRepository;

import com.sboot.repository.ProductionScheduleRepository;
 
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.ZoneId;

import java.util.Date;

import java.util.List;
 
import com.sboot.entity.*;
import com.sboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionScheduleService {

    @Autowired
    private ProductionScheduleRepository scheduleRepo;
    @Autowired
    private ProductionUnitRepository unitRepo;
    @Autowired
    private RawMaterialsRepository rawMaterialRepo;
    @Autowired
    private ProductsRepository productRepo;
    @Autowired
    private ProductRawMaterialMappingRepository mappingRepo;

    // ✅ Manual deadline setter
    @Transactional
    public ProductionSchedule setDeadline(Long scheduleId, LocalDate deadline) throws Exception {
        ProductionSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new Exception("Schedule not found"));
        schedule.setPsDeadline(deadline);
        return scheduleRepo.save(schedule);
    }

    // ✅ Manual deadline getter
    public LocalDate getDeadline(Long scheduleId) throws Exception {
        return scheduleRepo.findById(scheduleId)
                .map(ProductionSchedule::getPsDeadline)
                .orElse(null);
    }

    @Transactional
    public ProductionSchedule createSchedule(ProductionSchedule schedule) throws Exception {
        // ✅ 1. Validate Product
        Product product = productRepo.findById(schedule.getProduct().getProductsId())
                .orElseThrow(() -> new Exception("Product not found"));

        Category category = product.getCategory();
        if (category == null) {
            throw new Exception("Product not linked to category");
        }

        ProductionUnit assignedUnit = null;

        // ✅ 2. Scheduler provides a unit
        if (schedule.getProductionUnit() != null && schedule.getProductionUnit().getUnitId() != null) {
            assignedUnit = unitRepo.findById(schedule.getProductionUnit().getUnitId())
                    .orElseThrow(() -> new Exception("Production Unit not found"));

            if (!assignedUnit.getCategory().getCategoryId().equals(category.getCategoryId())) {
                throw new Exception("Selected unit does not belong to the product's category");
            }
            if (!assignedUnit.isAvailable()) {
                throw new Exception("Selected production unit is not available");
            }
        }
        // ✅ 3. Auto-assign if no unit provided
        else {
            List<ProductionUnit> units = unitRepo.findByCategory_CategoryId(category.getCategoryId());
            assignedUnit = units.stream()
                    .filter(ProductionUnit::isAvailable)
                    .findFirst()
                    .orElse(null);

            if (assignedUnit == null) {
                throw new Exception("No available production unit for this category");
            }
        }

        // ✅ 4. Check capacity
        if (schedule.getPsQuantity() > assignedUnit.getCapacity()) {
            schedule.setStatus("HOLD");
            schedule.setProductionUnit(null); // not assigned
            return scheduleRepo.save(schedule);
        }

        // ✅ 5. Check raw materials via mapping
        List<ProductRawMaterialMapping> mappings = mappingRepo.findByProduct_ProductsId(product.getProductsId());
        for (ProductRawMaterialMapping mapping : mappings) {
            RawMaterial material = mapping.getRawMaterial();
            int requiredQty = mapping.getQuantityRequired() * schedule.getPsQuantity();

            if (material.getRwQuantity() < requiredQty) {
                throw new Exception("Not enough raw material: " + material.getRwName());
            }
        }

        // ✅ 6. Deduct raw materials
        for (ProductRawMaterialMapping mapping : mappings) {
            RawMaterial material = mapping.getRawMaterial();
            int requiredQty = mapping.getQuantityRequired() * schedule.getPsQuantity();
            material.setRwQuantity(material.getRwQuantity() - requiredQty);
            rawMaterialRepo.save(material);
        }

        // ✅ 7. Assign unit & finalize
        schedule.setProductionUnit(assignedUnit);
        schedule.setStatus("CREATED");

        // ✅ 8. Auto-set start date
        LocalDate now = LocalDate.now();
        schedule.setPsStartDate(now);

        // ✅ 9. Auto-set deadline based on quantity
        int qty = schedule.getPsQuantity();
        if (qty < 100) {
            schedule.setPsDeadline(now.plusDays(3));
        } else if (qty <= 300) {
            schedule.setPsDeadline(now.plusDays(5));
        } else {
            schedule.setPsDeadline(now.plusDays(7));
        }

        assignedUnit.setAvailable(false);
        unitRepo.save(assignedUnit);

        return scheduleRepo.save(schedule);
    }

    public List<ProductionSchedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }

    @Transactional
    public ProductionSchedule markScheduleAsDone(Long scheduleId) throws Exception {
        ProductionSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new Exception("Schedule not found"));

        if (!"CREATED".equalsIgnoreCase(schedule.getStatus())) {
            throw new Exception("Only CREATED schedules can be marked as DONE");
        }

        schedule.setStatus("DONE");

        ProductionUnit unit = schedule.getProductionUnit();
        if (unit != null) {
            unit.setAvailable(true);
            unitRepo.save(unit);
        }

        return scheduleRepo.save(schedule);
    }

    @Transactional
    public ProductionSchedule cancelSchedule(Long scheduleId) throws Exception {
        ProductionSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new Exception("Schedule not found"));

        if (!"CREATED".equalsIgnoreCase(schedule.getStatus())) {
            throw new Exception("Only CREATED schedules can be cancelled");
        }

        // restore raw materials
        List<ProductRawMaterialMapping> mappings =
                mappingRepo.findByProduct_ProductsId(schedule.getProduct().getProductsId());

        for (ProductRawMaterialMapping mapping : mappings) {
            RawMaterial material = mapping.getRawMaterial();
            int requiredQty = mapping.getQuantityRequired() * schedule.getPsQuantity();
            material.setRwQuantity(material.getRwQuantity() + requiredQty);
            rawMaterialRepo.save(material);
        }

        schedule.setStatus("CANCELLED");

        ProductionUnit unit = schedule.getProductionUnit();
        if (unit != null) {
            unit.setAvailable(true);
            unitRepo.save(unit);
        }

        return scheduleRepo.save(schedule);
    }

    // ✅ Daily deadline checker (reminder at 9 AM)
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkDeadlines() {
        LocalDate today = LocalDate.now();
        List<ProductionSchedule> nearingDeadlines = scheduleRepo.findSchedulesByDeadline(today.plusDays(1));
        for (ProductionSchedule schedule : nearingDeadlines) {
            System.out.println("⚠ Reminder: Production schedule " + schedule.getPsId()
                    + " for product " + schedule.getProduct().getProductsName()
                    + " is nearing deadline: " + schedule.getPsDeadline());
            // Later: send email / notification if needed
        }
    }
}


