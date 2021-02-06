package com.dl2.fyp.dto.stock_in_trade;

import com.dl2.fyp.entity.StockInTrade;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StockInTradeDto extends StockInTrade {
    private String code;
    private BigDecimal currentPrice;
    private BigDecimal riskIndex;
    private BigDecimal expectedPrice;
    private BigDecimal profit;
    private BigDecimal profitPercentage;


    public StockInTradeDto(StockInTrade stockInTrade){
        this.setId(stockInTrade.getId());
        this.setAverageCost(stockInTrade.getAverageCost());
        this.setCode(stockInTrade.getStock().getCode());
        this.setNumOfShare(stockInTrade.getNumOfShare());
        this.setRiskIndex(stockInTrade.getStock().getRiskIndex());
        this.setExpectedPrice(stockInTrade.getStock().getRecommendSellPrice());
        this.setCurrentPrice(stockInTrade.getStock().getCurrentPrice());
        this.setProfit(
                this.getCurrentPrice()
                .subtract(stockInTrade.getAverageCost())
                .multiply(BigDecimal.valueOf(this.getNumOfShare()))
        );
        this.setProfitPercentage(
                this.getCurrentPrice()
                .subtract(stockInTrade.getAverageCost())
                .divide(stockInTrade.getAverageCost(),2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
        );
    }
}
