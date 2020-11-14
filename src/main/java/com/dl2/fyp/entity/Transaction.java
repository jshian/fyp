package com.dl2.fyp.entity;

import com.dl2.fyp.enums.TransactionActionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Getter
@Setter
@Entity(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Account accountIn;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Account accountOut;

    @OneToOne(cascade = CascadeType.PERSIST)
    private StockTrade stockTrade;

    @NotNull
    private Float amount;

    private Float accountInAmountAfter;

    private Float accountOutAmountAfter;

    private TransactionActionType action;
}
