package com.dl2.fyp.dto.stock_event;

import com.dl2.fyp.entity.StockEvent;
import lombok.Data;

@Data
public class StockEventDto extends StockEvent {
    private String code;

    public StockEventDto(StockEvent stockEvent){
        this.setLink(stockEvent.getLink());
        this.setSeverity(stockEvent.getSeverity());
        this.setDatetime(stockEvent.getDatetime());
        this.setTitle(stockEvent.getTitle());
        this.setContent(stockEvent.getContent());
        this.setCode(stockEvent.getStock().getCode());
    }
}
