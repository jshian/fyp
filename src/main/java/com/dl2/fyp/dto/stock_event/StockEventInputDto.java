package com.dl2.fyp.dto.stock_event;

import com.dl2.fyp.entity.StockEvent;
import lombok.Data;

@Data
public class StockEventInputDto extends StockEvent {
    private String code;
}
