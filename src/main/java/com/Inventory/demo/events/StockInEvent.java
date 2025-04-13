package com.Inventory.demo.events;
import com.Inventory.demo.dto.StockDTO;


public class StockInEvent extends StockEvent {
    public StockInEvent(StockDTO stockDTO, String username) {
        super(stockDTO, username);
    }
}
