package com.Inventory.demo.controller;

import com.Inventory.demo.model.Product;
import com.Inventory.demo.repositories.ProductRepository;
import com.Inventory.demo.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditService auditService;

    // ✅ GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // ✅ GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(product);
    }

    // ✅ POST create product with audit logging
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, Principal principal) {
        Product saved = productRepository.save(product);
        auditService.logAction(
                "PRODUCT_CREATED",
                "Product",
                saved.getId(),
                principal.getName(),
                "Created product: " + saved.getName()
        );
        return ResponseEntity.ok(saved);
    }

    // ✅ PUT update product with audit logging
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, Principal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());

        Product saved = productRepository.save(product);

        auditService.logAction(
                "PRODUCT_UPDATED",
                "Product",
                saved.getId(),
                principal.getName(),
                "Updated product: " + saved.getName()
        );

        return ResponseEntity.ok(saved);
    }

    // ✅ DELETE product with audit logging
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Principal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);

        auditService.logAction(
                "PRODUCT_DELETED",
                "Product",
                product.getId(),
                principal.getName(),
                "Deleted product: " + product.getName()
        );

        return ResponseEntity.ok("Product deleted");
    }
}
