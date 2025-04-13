package com.Inventory.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockSummaryDTO {
    private Long productId;
    private String productName;
    private String category;
    private int currentStock;
    private BigDecimal price;
    private int movementsCount;
}