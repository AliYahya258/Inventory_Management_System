package com.Inventory.demo.repositories;

import com.Inventory.demo.model.Store;
import com.Inventory.demo.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // ✅ Correct method name
    List<StockMovement> findByStoreAndDateBetween(Store store, LocalDateTime start, LocalDateTime end);

    List<StockMovement> findByStore(Store store);
}
