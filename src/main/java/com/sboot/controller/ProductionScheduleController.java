package com.sboot.controller;

import com.sboot.entity.ProductionSchedule;
import com.sboot.repository.ProductionScheduleRepository;
import com.sboot.service.ProductionScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

//Production Scheduling 
@RestController
@RequestMapping("/api/schedules")
public class ProductionScheduleController {

    @Autowired
    private ProductionScheduleService scheduleService;
    @Autowired
    private ProductionScheduleRepository scheduleRepo;



    // ✅ Create a  new schedule

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ProductionSchedule schedule) {
        try {
            ProductionSchedule saved = scheduleService.createSchedule(schedule);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

 // ✅ Set deadline manually
     @PutMapping("/{id}/deadline")
    public ResponseEntity<?> updateDeadline(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String deadline = request.get("psDeadline");
        if (deadline == null || deadline.isBlank()) {
            return ResponseEntity.badRequest().body("Deadline is missing");
        }

        LocalDate newDeadline;
        try {
            newDeadline = LocalDate.parse(deadline); // expects yyyy-MM-dd
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }

        ProductionSchedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setPsDeadline(newDeadline);
        scheduleRepo.save(schedule);

        return ResponseEntity.ok(schedule);
    }





    // ✅ Get deadline for a schedule
    @GetMapping("/{id}/deadline")
    public LocalDate getDeadline(@PathVariable Long id) throws Exception {
        return scheduleService.getDeadline(id);
    }
    
    //changes are done
    
    @GetMapping
    public List<ProductionSchedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    
    @PutMapping("/{id}/done")
    public ResponseEntity<ProductionSchedule> markScheduleDone(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(scheduleService.markScheduleAsDone(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ProductionSchedule> cancelSchedule(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(scheduleService.cancelSchedule(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
