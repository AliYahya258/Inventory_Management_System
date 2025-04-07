package com.Inventory.demo.repositories;

import com.Inventory.demo.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByStoreId(Long storeId);  // Fetch inventory by store
}
