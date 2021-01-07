package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SectorCategory sector;

    @OneToMany(mappedBy = "stock",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Valid
    @JsonIgnore
    private List<StockInTrade> stockInTradeList;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;

    @Column(name = "name")
    @NotNull
    private String name;

    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal currentPrice = new BigDecimal(0);
    @Range(min = 0, max = 1, message = "Out of range")
    @NotNull
    private BigDecimal riskIndex = new BigDecimal(0);
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal recommendBuyPrice = new BigDecimal(0);
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal recommendSellPrice = new BigDecimal(0);
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private Integer holdingPeriod = 0;
    @NotNull
    private BigDecimal expectedProfit = new BigDecimal(0);
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal dividendProfit = new BigDecimal(0);
    @Min(value = 0, message = "invalid negative input")
    @NotNull
    private BigDecimal volatilityProfit = new BigDecimal(0);
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @LastModifiedDate
    private Date lastUpdated = new Date();
}
