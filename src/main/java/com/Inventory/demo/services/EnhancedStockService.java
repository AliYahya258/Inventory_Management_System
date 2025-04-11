package com.Inventory.demo.services;

import com.Inventory.demo.config.EventConfig;
import com.Inventory.demo.dto.StockMovementDTO;
import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.Stock;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.StockRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnhancedStockService {

    private final StockRepository stockRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EnhancedStockService(StockRepository stockRepository, RabbitTemplate rabbitTemplate) {
        this.stockRepository = stockRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Cacheable(value = "stocks", key = "#product.id + '-' + #store.id")
    public Optional<Stock> getStockByProductAndStore(Product product, Store store) {
        return stockRepository.findByProductAndStore(product, store);
    }

    @Cacheable(value = "stocks", key = "#store.id")
    public List<Stock> getStocksByStore(Store store) {
        return stockRepository.findAllByStore(store);
    }

    @Transactional
    @CacheEvict(value = "stocks", key = "#product.id + '-' + #store.id")
    public Stock increaseStock(Product product, Store store, int quantity) {
        Stock stock = stockRepository.findByProductAndStore(product, store)
                .orElse(new Stock(product, store, 0));

        int previousQuantity = stock.getQuantity();
        stock.setQuantity(previousQuantity + quantity);
        Stock savedStock = stockRepository.save(stock);

        // Publish stock movement event
        StockMovementDTO event = new StockMovementDTO();
        event.setProductId(product.getId());
        event.setStoreId(store.getId());
        event.setQuantity(quantity);
        event.setType("IN");
        event.setDate(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_MOVEMENT_ROUTING_KEY,
                event
        );

        return savedStock;
    }

    @Transactional
    @CacheEvict(value = "stocks", key = "#product.id + '-' + #store.id")
    public Stock decreaseStock(Product product, Store store, int quantity) {
        Stock stock = stockRepository.findByProductAndStore(product, store)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (stock.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock to remove");
        }

        int previousQuantity = stock.getQuantity();
        stock.setQuantity(previousQuantity - quantity);
        Stock savedStock = stockRepository.save(stock);

        // Publish stock movement event
        StockMovementDTO event = new StockMovementDTO();
        event.setProductId(product.getId());
        event.setStoreId(store.getId());
        event.setQuantity(quantity);
        event.setType("OUT");
        event.setDate(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                EventConfig.INVENTORY_EXCHANGE,
                EventConfig.STOCK_MOVEMENT_ROUTING_KEY,
                event
        );

        return savedStock;
    }
}