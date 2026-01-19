package com.sboot.service;

//Trend analysis
import com.sboot.entity.ProductionUnit;
import com.sboot.repository.ProductionUnitRepository;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class TrackProductionProgressService {
 
    private final ProductionUnitRepository unitRepo;
 
    public TrackProductionProgressService(ProductionUnitRepository unitRepo) {
        this.unitRepo = unitRepo;
    }
 
    public List<ProductionUnit> getAllUnits() {
        return unitRepo.findAll();
    }
 
    public ProductionUnit getUnitById(Long id) {
        return unitRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
    }
 
    public ProductionUnit updateStatus(Long unitId, String status) {
        ProductionUnit unit = getUnitById(unitId);
        unit.setStatus(status);
        return unitRepo.save(unit);
    }
 
    public List<ProductionUnit> getUnitsByStatus(String status) {
        return unitRepo.findByStatus(status);
    }
    // Updated  Code .
}
 
 