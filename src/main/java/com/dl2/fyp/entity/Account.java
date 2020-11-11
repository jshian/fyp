package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity(name = "t_account")
public class Account {
    public enum AccountCategory{
        BANK,SHARE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<Transaction> transactionsList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<StockInTrade> stockTradeList = new LinkedList<>();

    @Enumerated(EnumType.STRING)
    private AccountCategory category;


}
