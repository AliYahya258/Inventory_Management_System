package com.Inventory.demo.controller;

import com.Inventory.demo.model.StockMovement;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.StockMovementRepository;
import com.Inventory.demo.repositories.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    private final StockMovementRepository stockMovementRepository;
    private final StoreRepository storeRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public StockMovementController(StockMovementRepository stockMovementRepository, StoreRepository storeRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.storeRepository = storeRepository;
    }

    // ✅ Log a stock movement
    @PostMapping
    public ResponseEntity<StockMovement> logStockMovement(@RequestBody StockMovement movement) {
        movement.setDate(LocalDateTime.now()); // Automatically sets the current timestamp
        return ResponseEntity.ok(stockMovementRepository.save(movement));
    }

    // ✅ Get stock movements for a store within a date range
    @GetMapping("/{storeId}")
    public ResponseEntity<List<StockMovement>> getStockMovements(
            @PathVariable Long storeId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        // 🔹 Convert date strings to LocalDateTime
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, FORMATTER);

        // 🔹 Fetch Store entity using storeId
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with ID: " + storeId));

        // 🔹 Fetch stock movements using the correct method
        List<StockMovement> movements = stockMovementRepository.findByStoreAndDateBetween(store, startDate, endDate);

        return ResponseEntity.ok(movements);
    }
}
