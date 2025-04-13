package com.Inventory.demo.listeners;

import com.Inventory.demo.config.EventConfig;
import com.Inventory.demo.dto.StockDTO;
import com.Inventory.demo.dto.StockMovementDTO;
import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.StockMovement;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.ProductRepository;
import com.Inventory.demo.repositories.StockMovementRepository;
import com.Inventory.demo.repositories.StoreRepository;
import com.Inventory.demo.services.AuditService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockEventListeners {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private AuditService auditService;

    // Listen for stock movements
    @RabbitListener(queues = EventConfig.STOCK_MOVEMENT_QUEUE)
    public void handleStockMovement(StockMovementDTO movementDTO) {
        try {
            Product product = productRepository.findById(movementDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Store store = storeRepository.findById(movementDTO.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));

            // Create and save movement record
            StockMovement movement = new StockMovement();
            movement.setProduct(product);
            movement.setStore(store);
            movement.setQuantity(movementDTO.getQuantity());
            movement.setType(movementDTO.getType());
            movement.setDate(movementDTO.getDate());

            stockMovementRepository.save(movement);
        } catch (Exception e) {
            // Log error - in production, you might want to use a more sophisticated error handling
            System.err.println("Error processing stock movement: " + e.getMessage());
        }
    }

    // Listen for stock updates (can be used for cache invalidation, notifications, etc.)
    @RabbitListener(queues = EventConfig.STOCK_UPDATE_QUEUE)
    public void handleStockUpdate(StockDTO stockDTO) {
        // This could be used for:
        // - Triggering notifications for low stock
        // - Invalidating caches
        // - Updating analytics
        // - Triggering orders for replenishment

        // For demonstration purposes, let's just log the event
        System.out.println("Stock update received for product ID: " + stockDTO.getProductId() +
                ", New quantity: " + stockDTO.getQuantity());

        // Example: Check if stock is low and needs replenishment
        if (stockDTO.getQuantity() < 10) {
            System.out.println("Low stock alert: Product ID " + stockDTO.getProductId() +
                    " at store " + stockDTO.getStoreId() +
                    " has low stock (" + stockDTO.getQuantity() + ")");
            // Here you could send an email, create a notification, etc.
        }
    }
}