package com.sboot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sboot.entity.Category;
import com.sboot.entity.ProductionUnit;
import com.sboot.repository.CategoriesRepository;
import com.sboot.repository.ProductionUnitRepository;

@RestController
@RequestMapping("/api/units")
public class ProductionUnitController {

    @Autowired
    private ProductionUnitRepository unitRepo;

    @Autowired
    private CategoriesRepository categoryRepo;

    //Based on unit it assigning belt 

    // ✅ Create a new Production Unit
    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody ProductionUnit unit) {
        try {

            //Category category = categoryRepo.findById(unit.getCategory().getCategoriesId())

            Category category = categoryRepo.findById(unit.getCategory().getCategoryId())

                    .orElseThrow(() -> new Exception("Category not found"));

            unit.setCategory(category);
            unit.setAvailable(true); // new unit is available
            ProductionUnit saved = unitRepo.save(unit);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get all unitssS
    @GetMapping
    public List<ProductionUnit> getAllUnits() {
        return unitRepo.findAll();
    }

    // ✅ Get units by categoriessss
    @GetMapping("/category/{catId}")
    public List<ProductionUnit> getUnitsByCategory(@PathVariable Long catId) {
        return unitRepo.findByCategory_CategoryId(catId);
    }
}
