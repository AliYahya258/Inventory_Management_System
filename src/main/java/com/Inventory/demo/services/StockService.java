package com.Inventory.demo.services;

import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.Stock;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Optional<Stock> getStockByProductAndStore(Product product, Store store) {
        return stockRepository.findByProductAndStore(product, store);
    }

    public Stock increaseStock(Product product, Store store, int quantity) {
        Stock stock = stockRepository.findByProductAndStore(product, store)
                .orElse(new Stock(product, store, 0));
        stock.setQuantity(stock.getQuantity() + quantity);
        return stockRepository.save(stock);
    }

    public Stock decreaseStock(Product product, Store store, int quantity) {
        Stock stock = stockRepository.findByProductAndStore(product, store)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        if (stock.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock to remove");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        return stockRepository.save(stock);
    }
}
