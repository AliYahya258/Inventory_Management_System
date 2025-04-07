package com.Inventory.demo.controller;

import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.StoreRepository;
import com.Inventory.demo.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AuditService auditService;

    // ✅ GET all stores
    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // ✅ GET store by ID
    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return ResponseEntity.ok(store);
    }

    // ✅ POST create store with audit logging
    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody Store store, Principal principal) {
        Store saved = storeRepository.save(store);

        auditService.logAction(
                "STORE_CREATED",
                "Store",
                saved.getId(),
                principal.getName(),
                "Created store: " + saved.getName()
        );

        return ResponseEntity.ok(saved);
    }

    // ✅ PUT update store with audit logging
    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store updatedStore, Principal principal) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        store.setName(updatedStore.getName());


        Store saved = storeRepository.save(store);

        auditService.logAction(
                "STORE_UPDATED",
                "Store",
                saved.getId(),
                principal.getName(),
                "Updated store: " + saved.getName()
        );

        return ResponseEntity.ok(saved);
    }

    // ✅ DELETE store with audit logging
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id, Principal principal) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        storeRepository.delete(store);

        auditService.logAction(
                "STORE_DELETED",
                "Store",
                store.getId(),
                principal.getName(),
                "Deleted store: " + store.getName()
        );

        return ResponseEntity.ok("Store deleted");
    }
}
