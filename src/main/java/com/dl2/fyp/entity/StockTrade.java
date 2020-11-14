package com.dl2.fyp.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity(name = "t_stock_trade")
public class StockTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private StockInTrade stock;

    @NotNull
    private Integer numOfShare;

    @NotNull
    private Float price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private float costAfter;

    private float profit;

    private float profitPercentage;

    @NotNull
    private Boolean action;

}
