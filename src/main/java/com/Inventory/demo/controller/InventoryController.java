package com.Inventory.demo.controller;

import com.Inventory.demo.model.Inventory;
import com.Inventory.demo.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public Inventory getItemById(@PathVariable Long id) {
        return inventoryService.getItemById(id);
    }

    @PostMapping
    public Inventory addItem(@RequestBody Inventory item) {
        return inventoryService.addItem(item);
    }

    @PutMapping("/{id}")
    public Inventory updateItem(@PathVariable Long id, @RequestBody Inventory item) {
        return inventoryService.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
    }
}
