package com.dl2.fyp.entity;

import com.dl2.fyp.enums.AccountCategory;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity(name = "t_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<Transaction> transactionsList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<StockInTrade> stockInTradesList = new LinkedList<>();

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    private float amount;
}
