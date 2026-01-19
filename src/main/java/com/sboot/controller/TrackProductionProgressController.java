package com.sboot.controller;
 
import com.sboot.entity.ProductionUnit;
import com.sboot.service.TrackProductionProgressService;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;

//Track production code updated
@RestController
@RequestMapping("/api/track-production")
public class TrackProductionProgressController {
 
    private final TrackProductionProgressService progressService;
 
    public TrackProductionProgressController(TrackProductionProgressService progressService) {
        this.progressService = progressService;
    }
 
    @GetMapping
    public List<ProductionUnit> getAllUnits() {
        return progressService.getAllUnits();
    }
 
    @GetMapping("/{id}")
    public ProductionUnit getUnit(@PathVariable Long id) {
        return progressService.getUnitById(id);
    }
 
    @PutMapping("/{id}/status")
    public ProductionUnit updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return progressService.updateStatus(id, status);
    }
 
    @GetMapping("/status/{status}")
    public List<ProductionUnit> getUnitsByStatus(@PathVariable String status) {
        return progressService.getUnitsByStatus(status);
    }
}
 
 