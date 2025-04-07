package com.Inventory.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "stock_movements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String type; // "IN", "OUT", "REMOVED"

    @Column(nullable = false)
    private LocalDateTime date;

}

