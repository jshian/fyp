package com.dl2.fyp.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@Entity(name = "t_stock_in_trade")
public class StockInTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    @NotNull
    private Integer numOfShare;

    @NotNull
    private Float averageCost;
}
