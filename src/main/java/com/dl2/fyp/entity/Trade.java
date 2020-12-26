package com.dl2.fyp.entity;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "t_trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trade_id")
    @Valid
    private List<StockInTrade> tradeList;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Long numOfShare;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal price;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @CreatedDate
    private Date date;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal costAfter;

    @NotNull
    private BigDecimal profit;

    @NotNull
    private BigDecimal profitPercentage;

    // 1 for buy 0 for sell
    @NotNull
    private Boolean action;

}
