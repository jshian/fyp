package com.dl2.fyp.dto.account;

import com.dl2.fyp.dto.stock_in_trade.StockInTradeForStatementDto;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.StockInTrade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountForStatementDto extends Account {

    private List<StockInTradeForStatementDto> stockInTrades = new ArrayList<>();

    @Override
    @JsonIgnore
    public List<StockInTrade> getStockInTradesList() {
        return null;
    }

    public AccountForStatementDto(Account account){
        this.setAmount(account.getAmount());
        this.setCategory(account.getCategory());
        this.setGrowthRate(account.getGrowthRate());
        this.setId(account.getId());
        for (StockInTrade stockInTrade : account.getStockInTradesList()) {
            stockInTrades.add(new StockInTradeForStatementDto(stockInTrade));
        }
    }
}
