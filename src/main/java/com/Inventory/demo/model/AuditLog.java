package com.Inventory.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // e.g., "STOCK_IN", "PRODUCT_CREATED"
    private String entityType; // e.g., "Product", "Stock"
    private Long entityId;

    private String performedBy; // username or userId
    private LocalDateTime timestamp;

    private String description; // optional details

    public AuditLog() {}

    public AuditLog(String action, String entityType, Long entityId, String performedBy, String description) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.performedBy = performedBy;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

}
