package com.Inventory.demo.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "stocks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Stock {
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

    public Stock(Product product, Store store, int quantity) {
        this.store = store;
        this.product = product;
        this.quantity = quantity;
    }
}


