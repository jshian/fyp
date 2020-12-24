package com.dl2.fyp.entity;

import com.dl2.fyp.enums.AccountCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity(name = "t_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    @JsonIgnore
    private List<Transaction> transactionsList;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    @JsonIgnore
    private List<StockInTrade> stockInTradesList;

    @Enumerated(EnumType.STRING)
    private AccountCategory category;

    private float amount;
}
