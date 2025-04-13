package com.Inventory.demo.events;

import com.Inventory.demo.dto.StockDTO;
import lombok.Getter;

public abstract class StockEvent {
    @Getter
    private final StockDTO stockDTO;
    @Getter
    private final String username;

    public StockEvent(StockDTO stockDTO, String username) {
        this.stockDTO = stockDTO;
        this.username = username;
    }
}