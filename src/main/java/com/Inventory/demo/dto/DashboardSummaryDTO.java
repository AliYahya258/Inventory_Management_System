package com.Inventory.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private int totalProducts;
    private int totalStores;
    private int lowStockProductsCount;
    private List<StoreSummaryDTO> storeSummaries;
}