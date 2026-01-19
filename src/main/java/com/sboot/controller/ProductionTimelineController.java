package com.sboot.controller;
 
import com.sboot.dto.ProductionTimelineResponse;

import com.sboot.service.ProductionTimelineService;

import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController

@RequestMapping("/api/production-timeline")

public class ProductionTimelineController {
 
    private final ProductionTimelineService timelineService;
 
    public ProductionTimelineController(ProductionTimelineService timelineService) {

        this.timelineService = timelineService;

    }
 
   

    @GetMapping("/{psId}")

    public List<ProductionTimelineResponse> getProductionTimeline(@PathVariable Long psId) {

        return timelineService.getProductionTimeline(psId);

    }



    @GetMapping

    public List<ProductionTimelineResponse> getAllProductionTimelines() {

        return timelineService.getAllProductionTimelines();

    }

}

 