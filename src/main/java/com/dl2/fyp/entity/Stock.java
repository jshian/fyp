package com.dl2.fyp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Data
@Getter
@Setter
@Entity(name = "t_stock")
public class Stock {
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
    private Float expectedProfit;
    private Float dividendProfit;
    private Float volatilityProfit;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
