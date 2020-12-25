package com.dl2.fyp.entity;

import com.dl2.fyp.enums.AccountCategory;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name = "t_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    private User user;

//    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @OrderColumn
//    private List<Transaction> transactionsList;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderColumn
    private List<Trade> tradeList;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderColumn
    private List<StockInTrade> stockInTradesList;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountCategory category;

    @NotNull
    private BigDecimal amount;
}
