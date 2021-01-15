package com.dl2.fyp.dto.stock_in_trade;

import com.dl2.fyp.entity.StockInTrade;
import lombok.Data;

@Data
public class StockInTradeForStatementDto {
    private String code;
    private Long stockInTradeId;

    public StockInTradeForStatementDto(StockInTrade stockInTrade){
        this.setStockInTradeId(stockInTrade.getId());
        this.setCode(stockInTrade.getStock().getCode());
    }
}
