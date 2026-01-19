package com.sboot.service;

import com.sboot.repository.ProductionScheduleRepository;
import com.sboot.repository.ProductionUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductionAnalyticsService {

    @Autowired
    private ProductionScheduleRepository scheduleRepo;

    @Autowired
    private ProductionUnitRepository unitRepo;

    public Map<String, Object> getProductionSummary() {
        Map<String, Object> data = new HashMap<>();

        // === Basic counts ===
        Long totalSchedules = scheduleRepo.count();
        Long completedSchedules = scheduleRepo.countByStatus("DONE");
        Long holdSchedules = scheduleRepo.countByStatus("HOLD");
        Long totalUnits = unitRepo.count();
        Long activeUnits = unitRepo.countByAvailable(false);

        // === Extended analytics ===
        Double avgDurationDays = scheduleRepo.getAverageProductionDuration();
        if (avgDurationDays == null) avgDurationDays = 0.0;

        Long delayedSchedules = scheduleRepo.countDelayedSchedules();
        if (delayedSchedules == null) delayedSchedules = 0L;

        // === Derived metrics ===
        double utilizationRate = (totalUnits != null && totalUnits > 0)
                ? (double) activeUnits / totalUnits * 100
                : 0.0;

        double completionRate = (totalSchedules != null && totalSchedules > 0)
                ? (double) completedSchedules / totalSchedules * 100
                : 0.0;

        // === Populate response ===
        data.put("totalSchedules", totalSchedules);
        data.put("completedSchedules", completedSchedules);
        data.put("holdSchedules", holdSchedules);
        data.put("delayedSchedules", delayedSchedules);
        data.put("averageProductionDays", avgDurationDays);
        data.put("completionRate", completionRate);
        data.put("totalUnits", totalUnits);
        data.put("activeUnits", activeUnits);
        data.put("unitUtilizationRate", utilizationRate);

        return data;
    }
}
