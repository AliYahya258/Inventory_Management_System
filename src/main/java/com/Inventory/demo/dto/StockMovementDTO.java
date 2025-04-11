package com.Inventory.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementDTO {
    private Long id;
    private Long storeId;
    private Long productId;
    private int quantity;
    private String type;  // "IN", "OUT", "REMOVED"
    private LocalDateTime date;
}