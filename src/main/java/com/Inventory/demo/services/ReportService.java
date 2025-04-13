package com.Inventory.demo.services;

import com.Inventory.demo.dto.*;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.model.StockMovement;
import com.Inventory.demo.repositories.StockMovementRepository;
import com.Inventory.demo.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Cacheable(value = "stockReports", key = "#filter.storeId + '-' + #filter.startDate + '-' + #filter.endDate")
    public StockReportResponseDTO generateStockReport(StockReportFilterDTO filter) {
        Store store = storeRepository.findById(filter.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        List<StockMovement> movements;
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            movements = stockMovementRepository.findByStoreAndDateBetween(
                    store, filter.getStartDate(), filter.getEndDate());
        } else {
            movements = stockMovementRepository.findByStore(store);
        }

        // Convert to DTOs
        List<StockMovementDTO> movementDTOs = movements.stream()
                .map(movement -> new StockMovementDTO(
                        movement.getId(),
                        movement.getStore().getId(),
                        movement.getProduct().getId(),
                        movement.getQuantity(),
                        movement.getType(),
                        movement.getDate()
                ))
                .collect(Collectors.toList());

        // Calculate totals
        int totalIn = 0;
        int totalOut = 0;

        for (StockMovement movement : movements) {
            if ("IN".equals(movement.getType())) {
                totalIn += movement.getQuantity();
            } else if ("OUT".equals(movement.getType()) || "REMOVED".equals(movement.getType())) {
                totalOut += movement.getQuantity();
            }
        }

        return new StockReportResponseDTO(
                store.getId(),
                store.getName(),
                filter.getStartDate(),
                filter.getEndDate(),
                movementDTOs,
                totalIn,
                totalOut
        );
    }
    @Cacheable(value = "dashboardSummary")
    public DashboardSummaryDTO getDashboardSummary() {
        // Implementation here to gather summary data across all stores
        // This would involve querying various repositories

        // For now, returning a placeholder
        return new DashboardSummaryDTO();
    }

    @Cacheable(value = "stockSummary", key = "#storeId")
    public List<ProductStockSummaryDTO> getProductStockSummary(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // This would be implemented with an optimal query or in-memory processing
        // For simplicity, I'm providing a placeholder implementation
        return new ArrayList<>(); // Replace with actual implementation
    }
}