package com.sboot.service;

import com.sboot.entity.ProductionSchedule;
import com.sboot.entity.ProductionUnit;
import com.sboot.repository.ProductionScheduleRepository;
import com.sboot.repository.ProductionUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductionEfficiencyReportService {

    private final ProductionScheduleRepository scheduleRepository;
    private final ProductionUnitRepository unitRepository;

    public ProductionEfficiencyReportService(ProductionScheduleRepository scheduleRepository,
                                             ProductionUnitRepository unitRepository) {
        this.scheduleRepository = scheduleRepository;
        this.unitRepository = unitRepository;
    }

    /**
     * Generate a detailed efficiency report for all or a specific production unit
     * within a date range.
     */
    public Map<String, Object> generateEfficiencyReport(Long unitId,
                                                        LocalDate startDate,
                                                        LocalDate endDate) {
        List<ProductionSchedule> schedules = scheduleRepository.findAll();

        // ✅ Filter schedules by provided criteria
        schedules = schedules.stream()
                .filter(s -> s.getPsStartDate() != null && !s.getPsStartDate().isBefore(startDate))
                .filter(s -> s.getPsEndDate() != null && !s.getPsEndDate().isAfter(endDate))
                .filter(s -> unitId == null ||
                        (s.getProductionUnit() != null &&
                                s.getProductionUnit().getUnitId().equals(unitId)))
                .toList();

        if (schedules.isEmpty()) {
            return Map.of("message", "No production data available for selected filters.");
        }

        long totalSchedules = schedules.size();
        long completedSchedules = schedules.stream()
                .filter(s -> "DONE".equalsIgnoreCase(s.getStatus()))
                .count();

        int totalOutput = schedules.stream()
                .mapToInt(s -> Optional.ofNullable(s.getPsQuantity()).orElse(0))
                .sum();

        // ✅ On-time delivery calculation
        long onTime = schedules.stream()
                .filter(s -> "DONE".equalsIgnoreCase(s.getStatus()))
                .filter(s -> s.getPsEndDate() != null &&
                        !s.getPsEndDate().isAfter(s.getPsDeadline()))
                .count();

        double onTimeRate = completedSchedules > 0
                ? (onTime * 100.0 / completedSchedules)
                : 0.0;

        // ✅ Average completion time
        double avgCompletionDays = schedules.stream()
                .filter(s -> s.getPsEndDate() != null && s.getPsStartDate() != null)
                .mapToDouble(s -> Duration.between(
                        s.getPsStartDate().atStartOfDay(),
                        s.getPsEndDate().atStartOfDay()).toDays())
                .average().orElse(0.0);

        // ✅ Efficiency score formula
        double efficiencyScore = (completedSchedules * 100.0 / totalSchedules)
                * (onTimeRate / 100.0)
                / (avgCompletionDays > 0 ? avgCompletionDays : 1);

        // ✅ Capacity utilization for the selected unit(s)
        double avgCapacityUtilization = schedules.stream()
                .filter(s -> s.getProductionUnit() != null)
                .mapToDouble(s -> (s.getPsQuantity() * 100.0)
                        / s.getProductionUnit().getCapacity())
                .average().orElse(0.0);

        // ✅ Unit status distribution
        Map<String, Long> statusBreakdown = schedules.stream()
                .collect(Collectors.groupingBy(
                        s -> Optional.ofNullable(s.getStatus()).orElse("UNKNOWN"),
                        Collectors.counting()
                ));

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("dataRange", startDate + " to " + endDate);
        report.put("unitId", unitId);
        report.put("totalSchedules", totalSchedules);
        report.put("completedSchedules", completedSchedules);
        report.put("totalOutputUnits", totalOutput);
        report.put("averageCompletionDays", String.format("%.2f", avgCompletionDays));
        report.put("onTimeDeliveryRate", String.format("%.2f%%", onTimeRate));
        report.put("capacityUtilization", String.format("%.2f%%", avgCapacityUtilization));
        report.put("efficiencyScore", String.format("%.2f", efficiencyScore * 100));
        report.put("statusBreakdown", statusBreakdown);

        return report;
    }

    /**
     * Generate summary for all production units collectively.
     */
    public List<Map<String, Object>> generateAllUnitsReport(LocalDate startDate, LocalDate endDate) {
        List<ProductionUnit> units = unitRepository.findAll();
        List<Map<String, Object>> reports = new ArrayList<>();

        for (ProductionUnit unit : units) {
            Map<String, Object> unitReport = generateEfficiencyReport(unit.getUnitId(), startDate, endDate);
            unitReport.put("unitName", unit.getUnitName());
            unitReport.put("unitStatus", unit.getStatus());
            reports.add(unitReport);
        }

        return reports;
    }
}
