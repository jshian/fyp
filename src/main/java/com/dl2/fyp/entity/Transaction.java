package com.dl2.fyp.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @CreatedDate
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountIn_id", referencedColumnName = "id")
    private Account accountIn;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "accountOut_id", referencedColumnName = "id")
    private Account accountOut;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal amount;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal accountInAmountAfter;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal accountOutAmountAfter;

}
