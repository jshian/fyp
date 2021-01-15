package com.dl2.fyp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity(name = "t_stock_in_trade")
public class StockInTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Stock stock;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_in_trade_id")
    @Valid
    private List<Trade> tradeList = new ArrayList<>();

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Long numOfShare;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal averageCost;
}
