package com.sboot.service;
import org.springframework.stereotype.Service;
import com.sboot.dto.InventoryReportFilterDTO;
import com.sboot.dto.InventoryTransactionReportDTO;
import com.sboot.dto.StockReportDTO;
import com.sboot.entity.InventoryTransaction;
import com.sboot.entity.Product;
import com.sboot.repository.InventoryTransactionRepository;
import com.sboot.repository.ProductsRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
//Rating done
@Service
public class InventoryReportService {

    private final ProductsRepository productsRepository;

    private final InventoryTransactionRepository transactionRepository;

    public InventoryReportService(ProductsRepository productRepository,

                                  InventoryTransactionRepository transactionRepository) {

        this.productsRepository = productRepository;

        this.transactionRepository = transactionRepository;

    }

    /**

     * Generate Stock Level Report with optional category and date filters

     */

    public List<StockReportDTO> getStockReport(InventoryReportFilterDTO filter) {

        List<Product> products = (List<Product>) productsRepository.findAll();

        return products.stream()

                // Filter by category if provided

                .filter(p -> filter.getCategoryId() == null ||

                             (p.getCategory() != null && p.getCategory().getCategoryId().equals(filter.getCategoryId())))

                // Filter by lastUpdated date range if provided

                .filter(p -> {

                    LocalDateTime lastUpdated = p.getLastUpdated();

                    boolean afterStart = filter.getStartDate() == null ||

                                         !lastUpdated.isBefore(filter.getStartDate().atStartOfDay());

                    boolean beforeEnd = filter.getEndDate() == null ||

                                        !lastUpdated.isAfter(filter.getEndDate().atTime(LocalTime.MAX));

                    return afterStart && beforeEnd;

                })

                .map(this::mapToStockReportDTO)

                .collect(Collectors.toList());

    }

    /**

     * Generate Inventory Transaction Report

     */

    public List<InventoryTransactionReportDTO> getTransactionReport() {

        List<InventoryTransaction> transactions = transactionRepository.findAll();

        return transactions.stream()

                .map(this::mapToTransactionReportDTO)

                .collect(Collectors.toList());

    }

    /**

     * Summary endpoint

     */

    public List<InventoryTransactionReportDTO> getTransactionReport(InventoryReportFilterDTO filter) {

        List<InventoryTransaction> transactions = transactionRepository.findAll();

        return transactions.stream()

                .filter(t -> filter.getCategoryId() == null ||

                             (t.getProduct() != null && t.getProduct().getCategory() != null &&

                              t.getProduct().getCategory().getCategoryId().equals(filter.getCategoryId())))

                .filter(t -> {

                    if (filter.getStartDate() == null && filter.getEndDate() == null) return true;

                    LocalDateTime txDate = t.getItTransactionDate() instanceof java.sql.Date ?

                            ((java.sql.Date) t.getItTransactionDate()).toLocalDate().atStartOfDay() :

                            LocalDateTime.ofInstant(t.getItTransactionDate().toInstant(), ZoneId.systemDefault());

                    boolean afterStart = filter.getStartDate() == null || !txDate.isBefore(filter.getStartDate().atStartOfDay());

                    boolean beforeEnd = filter.getEndDate() == null || !txDate.isAfter(filter.getEndDate().atTime(LocalTime.MAX));

                    return afterStart && beforeEnd;

                })

                .map(this::mapToTransactionReportDTO)

                .collect(Collectors.toList());

    }


    /** Mapping helpers */

    private StockReportDTO mapToStockReportDTO(Product p) {

        StockReportDTO dto = new StockReportDTO();

        dto.setProductId(p.getProductsId());

        dto.setProductName(p.getProductsName());

        dto.setProductDescription(p.getProductsDescription());

        dto.setCategoryName(p.getCategory() != null ? p.getCategory().getCategoryName() : null);

        dto.setUnitPrice(p.getProductsUnitPrice());

        dto.setQuantity(p.getProductsQuantity());

        dto.setMinThreshold(p.getMinThreshold());

        dto.setMaxThreshold(p.getMaxThreshold());

        dto.setLastUpdated(p.getLastUpdated());

        return dto;

    }

    private InventoryTransactionReportDTO mapToTransactionReportDTO(InventoryTransaction t) {

        InventoryTransactionReportDTO dto = new InventoryTransactionReportDTO();

        dto.setTransactionId(t.getItId());

        dto.setProductId(t.getProduct() != null ? t.getProduct().getProductsId() : null);

        dto.setProductName(t.getProduct() != null ? t.getProduct().getProductsName() : null);

        dto.setQuantity(t.getItQuantity());

        dto.setTransactionType(t.getItTransactionType());

        if (t.getItTransactionDate() != null) {

            if (t.getItTransactionDate() instanceof java.sql.Date) {

                // java.sql.Date → LocalDateTime

                java.sql.Date sqlDate = (java.sql.Date) t.getItTransactionDate();

                dto.setTransactionDate(

                    sqlDate.toLocalDate().atStartOfDay() // safe conversion

                );

            } else {

                // java.util.Date → LocalDateTime

                dto.setTransactionDate(

                    LocalDateTime.ofInstant(t.getItTransactionDate().toInstant(), ZoneId.systemDefault())

                );

            }

        } else {

            dto.setTransactionDate(null);

        }

        return dto;

    }


}

 