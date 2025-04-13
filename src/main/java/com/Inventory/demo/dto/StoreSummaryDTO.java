package com.Inventory.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreSummaryDTO {
    private Long storeId;
    private String storeName;
    private int productCount;
    private int totalStockValue;
    private int recentMovementsCount;
}