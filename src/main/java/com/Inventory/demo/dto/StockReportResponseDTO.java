package com.Inventory.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReportResponseDTO {
    private Long storeId;
    private String storeName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<StockMovementDTO> movements;
    private int totalInQuantity;
    private int totalOutQuantity;
}
