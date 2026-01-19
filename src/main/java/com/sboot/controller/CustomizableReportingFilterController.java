
//package com.sboot.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.sboot.dto.CustomizableReportingFilterDto;
//import com.sboot.entity.CustomizableReportingFilter;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/filters")
//public class CustomizableReportingFilterController<CustomizableReportingFilterService> {
//
//    private final CustomizableReportingFilterService service;
//
//    public CustomizableReportingFilterController(CustomizableReportingFilterService service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    public ResponseEntity<CustomizableReportingFilter> create(@RequestBody CustomizableReportingFilter dto) {
//        CustomizableReportingFilter created = service.create(dto);
//        return ResponseEntity.created(URI.create("/api/filters/" + created.getId())).body(created);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CustomizableReportingFilterDto> get(@PathVariable Long id) {
//        CustomizableReportingFilterDto d = ((Object) service).getById(id);
//        return d == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(d);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CustomizableReportingFilterDto>> list(
//            @RequestParam(required = false) Boolean onlyActive) {
//        return ResponseEntity.ok(service.listAll(onlyActive));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CustomizableReportingFilterDto> update(
//            @PathVariable Long id,
//            @RequestBody CustomizableReportingFilterDto dto) {
//
//        try {
//            return ResponseEntity.ok(service.update(id, dto));
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        service.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}
//package com.sboot.controller;
//
//import com.sboot.dto.CustomizableReportingFilterDto;
//import com.sboot.entity.CustomizableReportingFilter;
//import com.sboot.service.CustomizableReportingFilterService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/filters")
//public class CustomizableReportingFilterController {
//
//    private final CustomizableReportingFilterService service;
//
//    public CustomizableReportingFilterController(CustomizableReportingFilterService service) {
//        this.service = service;
//    }
//
//    // CREATE FILTER
//    @PostMapping
//    public ResponseEntity<CustomizableReportingFilterDto> create(
//            @RequestBody CustomizableReportingFilterDto dto) {
//
//        CustomizableReportingFilterDto created = service.create(dto);
//        return ResponseEntity
//                .created(URI.create("/api/filters/" + created.getId()))
//                .body(created);
//    }
//
//    // GET FILTER BY ID
//    @GetMapping("/{id}")
//    public ResponseEntity<CustomizableReportingFilterDto> get(@PathVariable Long id) {
//        CustomizableReportingFilterDto result = service.getById(id);
//        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
//    }
//
//    // LIST ALL FILTERS
//    @GetMapping
//    public ResponseEntity<List<CustomizableReportingFilterDto>> list(
//            @RequestParam(required = false) Boolean onlyActive) {
//
//        return ResponseEntity.ok(service.listAll(onlyActive));
//    }
//
//    // UPDATE FILTER
//    @PutMapping("/{id}")
//    public ResponseEntity<CustomizableReportingFilterDto> update(
//            @PathVariable Long id,
//            @RequestBody CustomizableReportingFilterDto dto) {
//
//        try {
//            return ResponseEntity.ok(service.update(id, dto));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // DELETE FILTER
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        service.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}
//
