package com.dl2.fyp.entity;

import com.dl2.fyp.enums.AccountCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name = "t_account")
public class Account {
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StockInTrade> stockInTradesList;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountCategory category;

    @NotNull
    @Min(value = 0,message = "invalid negative input")
    private BigDecimal amount = new BigDecimal(0);

    @NotNull
    @Min(value = 0,message = "invalid negative input")
    private BigDecimal growthRate = new BigDecimal(0);
}
