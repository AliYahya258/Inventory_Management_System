//package com.Inventory.demo.controller;
//
//import com.Inventory.demo.dto.ProductDTO;
//import com.Inventory.demo.model.Product;
//import com.Inventory.demo.repositories.ProductRepository;
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
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private AuditService auditService;
//
//    // GET all products
//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        List<ProductDTO> productDTOs = productRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(productDTOs);
//    }
//
//    // GET product by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        return ResponseEntity.ok(convertToDTO(product));
//    }
//
//    // POST create product with audit logging
//    @PostMapping
//    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO, Principal principal) {
//        Product product = convertToEntity(productDTO);
//        Product saved = productRepository.save(product);
//
//        auditService.logAction(
//                "PRODUCT_CREATED",
//                "Product",
//                saved.getId(),
//                principal.getName(),
//                "Created product: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(convertToDTO(saved));
//    }
//
//    // PUT update product with audit logging
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProductDTO, Principal principal) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Update product fields from DTO
//        product.setName(updatedProductDTO.getName());
//        product.setDescription(updatedProductDTO.getDescription());
//        product.setCategory(updatedProductDTO.getCategory());
//        product.setPrice(updatedProductDTO.getPrice());
//        product.setStockQuantity(updatedProductDTO.getStockQuantity());
//
//        Product saved = productRepository.save(product);
//
//        auditService.logAction(
//                "PRODUCT_UPDATED",
//                "Product",
//                saved.getId(),
//                principal.getName(),
//                "Updated product: " + saved.getName()
//        );
//
//        return ResponseEntity.ok(convertToDTO(saved));
//    }
//
//    // DELETE product with audit logging
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Principal principal) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        productRepository.delete(product);
//
//        auditService.logAction(
//                "PRODUCT_DELETED",
//                "Product",
//                product.getId(),
//                principal.getName(),
//                "Deleted product: " + product.getName()
//        );
//
//        return ResponseEntity.ok("Product deleted");
//    }
//
//    // Helper method to convert Entity to DTO
//    private ProductDTO convertToDTO(Product product) {
//        ProductDTO dto = new ProductDTO();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setCategory(product.getCategory());
//        dto.setPrice(product.getPrice());
//        dto.setStockQuantity(product.getStockQuantity());
//        dto.setDescription(product.getDescription());
//        return dto;
//    }
//
//    // Helper method to convert DTO to Entity
//    private Product convertToEntity(ProductDTO dto) {
//        Product product = new Product();
//        // Don't set ID for new entities, let DB generate it
//        if (dto.getId() != null) {
//            product.setId(dto.getId());
//        }
//        product.setName(dto.getName());
//        product.setCategory(dto.getCategory());
//        product.setPrice(dto.getPrice());
//        product.setStockQuantity(dto.getStockQuantity());
//        product.setDescription(dto.getDescription());
//        return product;
//    }
//}

package com.Inventory.demo.controller;

import com.Inventory.demo.dto.ApiResponse;
import com.Inventory.demo.dto.ProductDTO;
import com.Inventory.demo.model.Product;
import com.Inventory.demo.repositories.ProductRepository;
import com.Inventory.demo.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditService auditService;

    // GET all products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> productDTOs = productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(productDTOs));
    }

    // GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(product)));
    }

    // POST create product with audit logging
    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO, Principal principal) {
        Product product = convertToEntity(productDTO);
        Product saved = productRepository.save(product);

        auditService.logAction(
                "PRODUCT_CREATED",
                "Product",
                saved.getId(),
                principal.getName(),
                "Created product: " + saved.getName()
        );

        return ResponseEntity.ok(ApiResponse.success("Product created successfully", convertToDTO(saved)));
    }

    // PUT update product with audit logging
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO updatedProductDTO, Principal principal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product fields from DTO
        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setCategory(updatedProductDTO.getCategory());
        product.setPrice(updatedProductDTO.getPrice());
        product.setStockQuantity(updatedProductDTO.getStockQuantity());

        Product saved = productRepository.save(product);

        auditService.logAction(
                "PRODUCT_UPDATED",
                "Product",
                saved.getId(),
                principal.getName(),
                "Updated product: " + saved.getName()
        );

        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", convertToDTO(saved)));
    }

    // DELETE product with audit logging
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id, Principal principal) {
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

        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    // Helper method to convert Entity to DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setDescription(product.getDescription());
        return dto;
    }

    // Helper method to convert DTO to Entity
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        // Don't set ID for new entities, let DB generate it
        if (dto.getId() != null) {
            product.setId(dto.getId());
        }
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setDescription(dto.getDescription());
        return product;
    }
}