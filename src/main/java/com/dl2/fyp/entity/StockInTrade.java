package com.dl2.fyp.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Entity(name = "t_stock_in_trade")
public class StockInTrade {
    @OneToOne(cascade = CascadeType.PERSIST)
    private Account account;

    @OneToOne(cascade = CascadeType.PERSIST)
    private StockInfo stockInfo;

    @NotNull
    private Integer numOfShare;

    @NotNull
    private Float price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
