//package com.Inventory.demo.controller;
//
//import com.Inventory.demo.dto.StockDTO;
//import com.Inventory.demo.model.Product;
//import com.Inventory.demo.model.Stock;
//import com.Inventory.demo.model.StockMovement;
//import com.Inventory.demo.model.Store;
//import com.Inventory.demo.repositories.ProductRepository;
//import com.Inventory.demo.repositories.StockMovementRepository;
//import com.Inventory.demo.repositories.StockRepository;
//import com.Inventory.demo.repositories.StoreRepository;
//import com.Inventory.demo.services.AuditService;
//import com.Inventory.demo.services.EnhancedStockService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.util.List;

//@RestController
//@RequestMapping("/api/stocks")
//public class StockController {
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    @Autowired
//    private StockMovementRepository stockMovementRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private EnhancedStockService stockService;
//
//    @Autowired
//    private AuditService auditService;
//
//    // ✅ GET current stock level
//    @GetMapping("/level")
//    public ResponseEntity<?> getStockLevel(@RequestParam Long storeId, @RequestParam Long productId) {
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        Product product = productRepository.findById(productId).orElseThrow();
//        Stock stock = stockRepository.findByProductAndStore(product, store).orElse(null);
//        int quantity = (stock != null) ? stock.getQuantity() : 0;
//        return ResponseEntity.ok(quantity);
//    }
//
//    // ✅ GET stock movement report (store + optional date range)
//    @GetMapping("/report")
//    public ResponseEntity<List<StockMovement>> getReportByStoreAndDate(
//            @RequestParam Long storeId,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
//    ) {
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        List<StockMovement> report = (from != null && to != null)
//                ? stockMovementRepository.findByStoreAndDateBetween(store, from, to)
//                : stockMovementRepository.findByStore(store);
//        return ResponseEntity.ok(report);
//    }
//
//    // ✅ POST stock in with audit logging
//    @PostMapping("/in")
//    public ResponseEntity<?> stockIn(@RequestParam Long storeId,
//                                     @RequestParam Long productId,
//                                     @RequestParam int quantity,
//                                     Principal principal) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        Stock stock = stockService.increaseStock(product, store, quantity);
//
//        auditService.logAction(
//                "STOCK_IN", "Stock", stock.getId(), principal.getName(),
//                "Added " + quantity + " units of '" + product.getName() + "' to store '" + store.getName() + "'"
//        );
//
//        return ResponseEntity.ok("Stock increased");
//    }
//
//    // ✅ POST stock out with audit logging
//    @PostMapping("/out")
//    public ResponseEntity<?> stockOut(@RequestParam Long storeId,
//                                      @RequestParam Long productId,
//                                      @RequestParam int quantity,
//                                      Principal principal) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        Stock stock = stockService.decreaseStock(product, store, quantity);
//
//        auditService.logAction(
//                "STOCK_OUT", "Stock", stock.getId(), principal.getName(),
//                "Removed " + quantity + " units of '" + product.getName() + "' from store '" + store.getName() + "'"
//        );
//
//        return ResponseEntity.ok("Stock decreased");
//    }
//}

package com.Inventory.demo.controller;

import com.Inventory.demo.dto.StockDTO;
import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.Stock;
import com.Inventory.demo.model.StockMovement;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.ProductRepository;
import com.Inventory.demo.repositories.StockMovementRepository;
import com.Inventory.demo.repositories.StockRepository;
import com.Inventory.demo.repositories.StoreRepository;
import com.Inventory.demo.services.AuditService;
import com.Inventory.demo.services.EnhancedStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EnhancedStockService stockService;

    @Autowired
    private AuditService auditService;

    // ✅ GET current stock level
    @GetMapping("/level")
    public ResponseEntity<?> getStockLevel(@RequestParam Long storeId, @RequestParam Long productId) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        Stock stock = stockRepository.findByProductAndStore(product, store).orElse(null);
        int quantity = (stock != null) ? stock.getQuantity() : 0;
        return ResponseEntity.ok(quantity);
    }

    // ✅ GET stock movement report (store + optional date range)
    @GetMapping("/report")
    public ResponseEntity<List<StockMovement>> getReportByStoreAndDate(
            @RequestParam Long storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        List<StockMovement> report = (from != null && to != null)
                ? stockMovementRepository.findByStoreAndDateBetween(store, from, to)
                : stockMovementRepository.findByStore(store);
        return ResponseEntity.ok(report);
    }

    // ✅ POST stock in with audit logging and use of DTOs
//    @PostMapping("/in")
//    public ResponseEntity<?> stockIn(@RequestParam Long storeId,
//                                     @RequestParam Long productId,
//                                     @RequestParam int quantity,
//                                     Principal principal) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        Store store = storeRepository.findById(storeId).orElseThrow();
//
//        // Increase stock and return StockDTO
//        StockDTO stockDTO = stockService.increaseStock(product, store, quantity);
//
//        // Log audit action
//        auditService.logAction(
//                "STOCK_IN", "Stock", stockDTO.getId(), principal.getName(),
//                "Added " + quantity + " units of '" + product.getName() + "' to store '" + store.getName() + "'"
//        );
//
//        return ResponseEntity.ok(stockDTO);
//    }
    @PostMapping("/in")
    public ResponseEntity<?> stockIn(@RequestParam Long storeId,
                                     @RequestParam Long productId,
                                     @RequestParam int quantity,
                                     Principal principal) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Store not found"));

            // Service now returns StockDTO directly
            StockDTO stockDTO = stockService.increaseStock(product, store, quantity);

            auditService.logAction(
                    "STOCK_IN", "Stock", stockDTO.getId(), principal.getName(),
                    "Added " + quantity + " units of '" + product.getName() + "' to store '" + store.getName() + "'"
            );

            return ResponseEntity.ok(stockDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ POST stock out with audit logging and use of DTOs
    @PostMapping("/out")
    public ResponseEntity<?> stockOut(@RequestParam Long storeId,
                                      @RequestParam Long productId,
                                      @RequestParam int quantity,
                                      Principal principal) {
        Product product = productRepository.findById(productId).orElseThrow();
        Store store = storeRepository.findById(storeId).orElseThrow();

        // Decrease stock and return StockDTO
        StockDTO stockDTO = stockService.decreaseStock(product, store, quantity);

        // Log audit action
        auditService.logAction(
                "STOCK_OUT", "Stock", stockDTO.getId(), principal.getName(),
                "Removed " + quantity + " units of '" + product.getName() + "' from store '" + store.getName() + "'"
        );

        return ResponseEntity.ok(stockDTO);
    }
}

