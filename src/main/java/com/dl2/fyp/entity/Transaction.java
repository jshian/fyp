package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountIn_id", referencedColumnName = "id")
    private Account accountIn;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "accountOut_id", referencedColumnName = "id")
    private Account accountOut;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal accountInAmountAfter;

    @NotNull
    private BigDecimal accountOutAmountAfter;

    @NotNull
    private Boolean action;
}
