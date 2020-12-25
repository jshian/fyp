package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "t_stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SectorCategory sector;

    @OneToMany(mappedBy = "stock",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderColumn
    private List<StockInTrade> stockInTradeList;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;

    private BigDecimal currentPrice;
    private BigDecimal riskIndex;
    private BigDecimal recommendBuyPrice;
    private BigDecimal recommendSellPrice;
    private Integer holdingPeriod;
    private BigDecimal expectedProfit;
    private BigDecimal dividendProfit;
    private BigDecimal volatilityProfit;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpdated;
}
