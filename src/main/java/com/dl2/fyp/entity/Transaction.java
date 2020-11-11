package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Entity(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


//    private Integer accountIn;
//    private Integer accountOut;
    @NotNull
    private Integer amount;


    private Integer unitPrice;
//    private Integer accountInAmountAfter;
//    private Integer accountOutAmountAfter;


    private Boolean action;
}
