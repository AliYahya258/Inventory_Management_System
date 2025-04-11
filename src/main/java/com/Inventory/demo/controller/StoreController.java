//package com.Inventory.demo.controller;
//
//import com.Inventory.demo.model.Store;
//import com.Inventory.demo.repositories.StoreRepository;
//import com.Inventory.demo.services.AuditService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/stores")
//public class StoreController {
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private AuditService auditService;
//
//    // ✅ GET all stores
//    @GetMapping
//    public List<Store> getAllStores() {
//        return storeRepository.findAll();
//    }
//
//    // ✅ GET store by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//        return ResponseEntity.ok(store);
//    }
//
//    // ✅ POST create store with audit logging
//    @PostMapping
//    public ResponseEntity<Store> createStore(@RequestBody Store store, Principal principal) {
//        Store saved = storeRepository.save(store);
//
//        auditService.logAction(
//                "STORE_CREATED",
//                "Store",
//                saved.getId(),
//                principal.getName(),
//                "Created store: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(saved);
//    }
//
//    // ✅ PUT update store with audit logging
//    @PutMapping("/{id}")
//    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store updatedStore, Principal principal) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//
//        store.setName(updatedStore.getName());
//
//
//        Store saved = storeRepository.save(store);
//
//        auditService.logAction(
//                "STORE_UPDATED",
//                "Store",
//                saved.getId(),
//                principal.getName(),
//                "Updated store: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(saved);
//    }
//
//    // ✅ DELETE store with audit logging
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteStore(@PathVariable Long id, Principal principal) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//
//        storeRepository.delete(store);
//
//        auditService.logAction(
//                "STORE_DELETED",
//                "Store",
//                store.getId(),
//                principal.getName(),
//                "Deleted store: " + store.getName()
//        );
//
//        return ResponseEntity.ok("Store deleted");
//    }
//}

//package com.Inventory.demo.controller;
//
//import com.Inventory.demo.dto.StoreDTO;
//import com.Inventory.demo.model.Store;
//import com.Inventory.demo.repositories.StoreRepository;
//import com.Inventory.demo.services.AuditService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/stores")
//public class StoreController {
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private AuditService auditService;
//
//    // ✅ GET all stores
//    @GetMapping
//    public List<StoreDTO> getAllStores() {
//        return storeRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // ✅ GET store by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//        return ResponseEntity.ok(convertToDTO(store));
//    }
//
//    // ✅ POST create store with audit logging
//    @PostMapping
//    public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO storeDTO, Principal principal) {
//        Store store = convertToEntity(storeDTO);
//        Store saved = storeRepository.save(store);
//
//        auditService.logAction(
//                "STORE_CREATED",
//                "Store",
//                saved.getId(),
//                principal.getName(),
//                "Created store: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(convertToDTO(saved));
//    }
//
//    // ✅ PUT update store with audit logging
//    @PutMapping("/{id}")
//    public ResponseEntity<StoreDTO> updateStore(@PathVariable Long id, @RequestBody StoreDTO storeDTO, Principal principal) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//
//        // Update only the fields you want to change
//        store.setName(storeDTO.getName());
//        // The ID is already set correctly from the database, so no need to modify it
//
//        Store saved = storeRepository.save(store);
//
//        auditService.logAction(
//                "STORE_UPDATED",
//                "Store",
//                saved.getId(),
//                principal.getName(),
//                "Updated store: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(convertToDTO(saved));
//    }
//
//    // ✅ DELETE store with audit logging
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteStore(@PathVariable Long id, Principal principal) {
//        Store store = storeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//
//        storeRepository.delete(store);
//
//        auditService.logAction(
//                "STORE_DELETED",
//                "Store",
//                store.getId(),
//                principal.getName(),
//                "Deleted store: " + store.getName()
//        );
//
//        return ResponseEntity.ok("Store deleted");
//    }
//
//    // Helper methods to convert between Entity and DTO
//    private StoreDTO convertToDTO(Store store) {
//        return new StoreDTO(store.getId(), store.getName());
//    }
//
//    private Store convertToEntity(StoreDTO storeDTO) {
//        Store store = new Store();
//        store.setName(storeDTO.getName());
//        return store;
//    }
//
//}

package com.Inventory.demo.controller;

import com.Inventory.demo.dto.StoreDTO;
import com.Inventory.demo.model.Store;
import com.Inventory.demo.repositories.StoreRepository;
import com.Inventory.demo.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AuditService auditService;

    // GET all stores
    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        List<StoreDTO> storeDTOs = storeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(storeDTOs);
    }

    // GET store by ID
    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return ResponseEntity.ok(convertToDTO(store));
    }

    // POST create store with audit logging
    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO storeDTO, Principal principal) {
        Store store = convertToEntity(storeDTO); // ID will not be set
        Store saved = storeRepository.save(store);

        auditService.logAction(
                "STORE_CREATED",
                "Store",
                saved.getId(),
                principal.getName(),
                "Created store: " + saved.getName()
        );

        return ResponseEntity.ok(convertToDTO(saved));
    }

    // PUT update store with audit logging
    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Long id, @RequestBody StoreDTO storeDTO, Principal principal) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Update mutable fields only
        store.setName(storeDTO.getName());

        Store saved = storeRepository.save(store);

        auditService.logAction(
                "STORE_UPDATED",
                "Store",
                saved.getId(),
                principal.getName(),
                "Updated store: " + saved.getName()
        );

        return ResponseEntity.ok(convertToDTO(saved));
    }

    // DELETE store with audit logging
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

    // Helper method to convert Entity to DTO
    private StoreDTO convertToDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        return dto;
    }

    // Helper method to convert DTO to Entity
    private Store convertToEntity(StoreDTO dto) {
        Store store = new Store();
        // Never set ID to preserve auto-generation
        store.setName(dto.getName());
        return store;
    }
}
