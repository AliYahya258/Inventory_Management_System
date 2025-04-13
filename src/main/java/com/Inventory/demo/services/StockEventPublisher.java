package com.Inventory.demo.services;

import com.Inventory.demo.config.EventConfig;
import com.Inventory.demo.dto.StockMovementDTO;
import com.Inventory.demo.events.StockEvent;
import com.Inventory.demo.events.StockInEvent;
import com.Inventory.demo.events.StockOutEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StockEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishStockEvent(StockEvent event) {
        if (event instanceof StockInEvent) {
            publishStockInEvent((StockInEvent) event);
        } else if (event instanceof StockOutEvent) {
            publishStockOutEvent((StockOutEvent) event);
        }
    }

    private void publishStockInEvent(StockInEvent event) {
        // For stock movement history
        StockMovementDTO movementDTO = new StockMovementDTO(
                event.getStockDTO().getStoreId(),
                event.getStockDTO().getProductId(),
                event.getStockDTO().getQuantity(),
                "IN",
                LocalDateTime.now()
        );

        // Send to stock movement queue for logging
        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_MOVEMENT_ROUTING_KEY,
                movementDTO
        );

        // Send to stock update queue for any other listeners
        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_UPDATE_ROUTING_KEY,
                event.getStockDTO()
        );
    }

    private void publishStockOutEvent(StockOutEvent event) {
        // For stock movement history
        StockMovementDTO movementDTO = new StockMovementDTO(
                event.getStockDTO().getStoreId(),
                event.getStockDTO().getProductId(),
                event.getStockDTO().getQuantity(),
                "OUT",
                LocalDateTime.now()
        );

        // Send to stock movement queue for logging
        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_MOVEMENT_ROUTING_KEY,
                movementDTO
        );

        // Send to stock update queue for any other listeners
        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_UPDATE_ROUTING_KEY,
                event.getStockDTO()
        );
    }
}