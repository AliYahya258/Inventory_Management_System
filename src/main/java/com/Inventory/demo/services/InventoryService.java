package com.Inventory.demo.services;

import com.Inventory.demo.model.Inventory;
import com.Inventory.demo.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    public Inventory getItemById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Inventory addItem(Inventory item) {
        return inventoryRepository.save(item);
    }

    public Inventory updateItem(Long id, Inventory updatedItem) {
        Inventory item = getItemById(id);
        item.setName(updatedItem.getName());
        item.setQuantity(updatedItem.getQuantity());
        item.setPrice(updatedItem.getPrice());
        return inventoryRepository.save(item);
    }

    public void deleteItem(Long id) {
        inventoryRepository.deleteById(id);
    }
}
