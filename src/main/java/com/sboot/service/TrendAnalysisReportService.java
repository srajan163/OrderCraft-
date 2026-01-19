package com.sboot.service;

import com.sboot.entity.InventoryTransaction;
import com.sboot.entity.PurchaseOrder;
import com.sboot.repository.InventoryTransactionRepository;

import com.sboot.repository.PurchaseOrdersRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TrendAnalysisReportService {

    private final InventoryTransactionRepository inventoryRepo;
    private final PurchaseOrdersRepository purchaseOrderRepo;

    public TrendAnalysisReportService(InventoryTransactionRepository inventoryRepo,
                                      PurchaseOrdersRepository purchaseOrderRepo) {
        this.inventoryRepo = inventoryRepo;
        this.purchaseOrderRepo = purchaseOrderRepo;
    }

    public Map<String, Object> generateTrendReport(String metric, String start, String end) {

        Map<String, Object> result = new HashMap<>();

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        if (metric.equalsIgnoreCase("stock-usage")) {
            List<InventoryTransaction> data =
                    inventoryRepo.findByDateRange(startDate, endDate);
            result.put("metric", "stockUsage");
            result.put("data", data);
        }

        if (metric.equalsIgnoreCase("supplier-performance")) {
            List<PurchaseOrder> data =
                    purchaseOrderRepo.findByDateRange(startDate.atStartOfDay(), endDate.atTime(23, 59));
            result.put("metric", "supplierPerformance");
            result.put("data", data);
        }

        return result;
    }
}
