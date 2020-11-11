package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity(name = "t_stock_info")
public class StockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", length = 6)
    private String code;
    private Integer amountPerShare;
    private Float currentPrice;
    private Float riskIndex;
    private Float recommendBuyPrice;
    private Float recommendSellPrice;
    private Integer holdingPeriod;
    private Double expectedProfit;
    private Double dividendProfit;
    private Double volatilityProfit;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
