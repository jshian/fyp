package com.dl2.fyp.entity;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_trade")
@Table(indexes = @Index(name = "date", columnList = "date"))
@EntityListeners(AuditingEntityListener.class)
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Long numOfShare;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Long totalShareAfter;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal price;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @CreatedDate
    private Date date;

    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal costAfter;

    @NotNull
    private BigDecimal profit;

    @NotNull
    private BigDecimal profitPercentage;

    // 1 for buy 0 for sell
    @NotNull
    private Boolean action;

}
