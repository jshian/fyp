package com.dl2.fyp.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @NotNull
    private Long numOfShare;

    @NotNull
    private BigDecimal price;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;

    private BigDecimal costAfter;

    private BigDecimal profit;

    private BigDecimal profitPercentage;

    @NotNull
    private Boolean action;

}
