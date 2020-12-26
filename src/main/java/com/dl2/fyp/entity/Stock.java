package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    @Valid
    private List<StockInTrade> stockInTradeList;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;

    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal currentPrice;
    @Range(min = 0, max = 1, message = "Out of range")
    @NotNull
    private BigDecimal riskIndex;
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal recommendBuyPrice;
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal recommendSellPrice;
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private Integer holdingPeriod;
    @NotNull
    private BigDecimal expectedProfit;
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal dividendProfit;
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal volatilityProfit;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @LastModifiedDate
    private Date lastUpdated;
}
