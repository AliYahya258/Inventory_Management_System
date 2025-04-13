package com.Inventory.demo.repositories;

import com.Inventory.demo.model.Product;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // ✅ Correct method name
    List<StockMovement> findByStoreAndDateBetween(Store store, LocalDateTime start, LocalDateTime end);

    List<StockMovement> findByStore(Store store);

    List<StockMovement> findByStoreAndProduct(Store store, Product product);

    List<StockMovement> findByStoreAndProductAndDateBetween(
            Store store, Product product, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.store.id = ?1 AND sm.product.id = ?2")
    int countMovementsByStoreAndProduct(Long storeId, Long productId);
}
