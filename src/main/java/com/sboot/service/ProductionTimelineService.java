package com.sboot.service;

import com.sboot.dto.ProductionTimelineResponse;
import com.sboot.entity.ProductionSchedule;
import com.sboot.repository.ProductionScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ProductionTimelineService {

    private final ProductionScheduleRepository scheduleRepository;

    public ProductionTimelineService(ProductionScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 🔹 Helper to convert LocalDate -> LocalDateTime

//    private LocalDateTime toDateTime(LocalDate localDateTime) {
//        return (localDateTime != null) ? localDateTime.atStartOfDay() : null;
//    }

    
//    private ProductionTimelineResponse mapToDto(ProductionSchedule schedule) {
//        ProductionTimelineResponse dto = new ProductionTimelineResponse();
//        dto.setStatus(schedule.getStatus());
//        dto.setUpdatedBy("system");
//
//        dto.setUpdatedAt(
//            schedule.getPsEndDate() != null
//                ? schedule.getPsEndDate()
//                : schedule.getPsStartDate()
//        );


    private LocalDateTime toDateTime(LocalDate date) {
        return (date != null) ? date.atStartOfDay() : null;
    }

    // 🔹 Mapper: Entity -> DTO
    private ProductionTimelineResponse mapToDto(ProductionSchedule schedule) {
        ProductionTimelineResponse dto = new ProductionTimelineResponse();
        dto.setStatus(schedule.getStatus());
        dto.setUpdatedBy("system"); // placeholder since entity doesn’t have user info
        dto.setUpdatedAt(schedule.getPsEndDate() != null
                ? toDateTime(schedule.getPsEndDate())
                : toDateTime(schedule.getPsStartDate()));

        dto.setRemarks("Schedule for product ID " + schedule.getProduct().getProductsId());
        dto.setQuantity(schedule.getPsQuantity() != null ? schedule.getPsQuantity() : 0);
        return dto;
    }

    // 🔹 Single Production Schedule Timeline
    public List<ProductionTimelineResponse> getProductionTimeline(Long psId) {
        return scheduleRepository.findById(psId)
                .map(schedule -> Collections.singletonList(mapToDto(schedule)))
                .orElse(Collections.emptyList());
    }

    // 🔹 All Production Schedule Timelines
    public List<ProductionTimelineResponse> getAllProductionTimelines() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}
