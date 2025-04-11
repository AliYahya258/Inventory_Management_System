package com.Inventory.demo.controller;

import com.Inventory.demo.dto.InventoryDTO;
import com.Inventory.demo.model.Inventory;
import com.Inventory.demo.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryDTO> getAllItems() {
        return inventoryService.getAllItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InventoryDTO getItemById(@PathVariable Long id) {
        return convertToDTO(inventoryService.getItemById(id));
    }

    @PostMapping
    public InventoryDTO addItem(@RequestBody InventoryDTO itemDTO) {
        Inventory saved = inventoryService.addItem(convertToEntity(itemDTO));
        return convertToDTO(saved);
    }

    @PutMapping("/{id}")
    public InventoryDTO updateItem(@PathVariable Long id, @RequestBody InventoryDTO itemDTO) {
        Inventory updated = inventoryService.updateItem(id, convertToEntity(itemDTO));
        return convertToDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
    }

    // ✅ Conversion methods
    private InventoryDTO convertToDTO(Inventory inventory) {
        return new InventoryDTO(
                inventory.getId(),
                inventory.getName(),
                inventory.getQuantity(),
                inventory.getPrice(),
                inventory.getStore() != null ? inventory.getStore().getId() : null
        );
    }

    private Inventory convertToEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        //inventory.setId(dto.getId()); // Leave this null if you're relying on auto-generation
        inventory.setName(dto.getName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setPrice(dto.getPrice());
        // Set store via service or repository in service layer (not here for now)
        return inventory;
    }
}
