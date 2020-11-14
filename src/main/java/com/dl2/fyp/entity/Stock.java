package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity(name = "t_stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", length = 6)
    private String code;
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
