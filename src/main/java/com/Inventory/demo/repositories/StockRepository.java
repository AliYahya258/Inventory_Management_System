package com.Inventory.demo.repositories;

import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Inventory.demo.model.Stock;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductAndStore(Product product, Store store);

    List<Stock> findAllByStore(Store store);
}


