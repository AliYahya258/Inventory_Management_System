package com.Inventory.demo.events;
import com.Inventory.demo.dto.StockDTO;

public class StockOutEvent extends StockEvent {
    public StockOutEvent(StockDTO stockDTO, String username) {
        super(stockDTO, username);
    }
}
