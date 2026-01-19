package com.sboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sboot.entity.PurchaseOrder;
import com.sboot.repository.PurchaseOrdersRepository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderFulfillmentReportService {

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    /**
     * Try to inject Spring's ObjectMapper. If it's not available for some reason,
     * we instantiate a fallback mapper in initMapper().
     */
    @Autowired(required = false)
    private ObjectMapper mapper;

    @PostConstruct
    public void initMapper() {
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
        }
        // Ensure JavaTimeModule is registered and we write ISO strings (not timestamps)
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Accepts an optional filter map with keys like "startDate", "endDate", "status", "supplierId", "internal".
     * Returns a list of maps representing every property of each PurchaseOrder (Jackson serializes the entity).
     */
    public List<Map<String, Object>> getFullPurchaseOrderMaps(Map<String, Object> filter) {
        List<PurchaseOrder> orders = purchaseOrdersRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            orders = applyFiltersInMemory(orders, filter);
        }

        // Convert each PurchaseOrder entity to a Map<String,Object> using Jackson.
        List<Map<String, Object>> rows = orders.stream()
                .map(o -> mapper.convertValue(o, Map.class))
                .collect(Collectors.toList());

        return rows;
    }

    private List<PurchaseOrder> applyFiltersInMemory(List<PurchaseOrder> orders, Map<String, Object> filter) {
        String status = filter.get("status") == null ? null : filter.get("status").toString();
        Long supplierId = null;
        if (filter.get("supplierId") != null) {
            try { supplierId = Long.parseLong(filter.get("supplierId").toString()); } catch (NumberFormatException ignored) {}
        }
        Boolean internal = null;
        if (filter.get("internal") != null) {
            internal = Boolean.valueOf(filter.get("internal").toString());
        }

        LocalDate start = null;
        LocalDate end = null;
        try {
            if (filter.get("startDate") != null) start = LocalDate.parse(filter.get("startDate").toString());
            if (filter.get("endDate") != null) end = LocalDate.parse(filter.get("endDate").toString());
        } catch (DateTimeParseException ignored) {}

        // Make final copies for use in lambda (required by Java)
        final String statusLowerFinal = status == null ? null : status.toLowerCase();
        final Long supplierIdFinal = supplierId;
        final Boolean internalFinal = internal;
        final LocalDate startFinal = start;
        final LocalDate endFinal = end;

        return orders.stream()
                .filter(o -> {
                    // supplier filter
                    if (supplierIdFinal != null) {
                        if (o.getSupplier() == null || !Objects.equals(o.getSupplier().getSuppliersId(), supplierIdFinal)) return false;
                    }
                    // internal filter (assumes getter getPoInternal() or isPoInternal())
                    if (internalFinal != null) {
                        Boolean poInternal = null;
                        try {
                            poInternal = (Boolean) PurchaseOrder.class.getMethod("getPoInternal").invoke(o);
                        } catch (Exception e1) {
                            try {
                                poInternal = (Boolean) PurchaseOrder.class.getMethod("isPoInternal").invoke(o);
                            } catch (Exception ignored) {}
                        }
                        if (poInternal == null || !poInternal.equals(internalFinal)) return false;
                    }
                    // status filter
                    if (statusLowerFinal != null && o.getPoDeliveryStatus() != null) {
                        if (!o.getPoDeliveryStatus().toLowerCase().contains(statusLowerFinal)) return false;
                    }
                    // date filters (assumes getPoOrderDate() returns LocalDateTime)
                    if (startFinal != null || endFinal != null) {
                        try {
                            java.time.LocalDate orderDate = o.getPoOrderDate() == null ? null : o.getPoOrderDate().toLocalDate();
                            if (startFinal != null && (orderDate == null || orderDate.isBefore(startFinal))) return false;
                            if (endFinal != null && (orderDate == null || orderDate.isAfter(endFinal))) return false;
                        } catch (Exception ignored) {}
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
