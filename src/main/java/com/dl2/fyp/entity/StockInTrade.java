package com.dl2.fyp.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity(name = "t_stock_in_trade")
public class StockInTrade {
    @OneToOne(cascade = CascadeType.PERSIST)
    private Account account;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Stock stock;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<StockTrade> stockTradesList = new LinkedList<>();

    @NotNull
    private Integer numOfShare;

    @NotNull
    private Float averageCost;
}
