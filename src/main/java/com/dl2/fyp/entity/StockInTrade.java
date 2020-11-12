package com.dl2.fyp.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Getter
@Setter
@Entity(name = "t_stock_in_trade")
public class StockInTrade {
    @OneToOne(cascade = CascadeType.PERSIST)
    private Account account;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Stock stock;

    @NotNull
    private Integer numOfShare;

    @NotNull
    private Float price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
