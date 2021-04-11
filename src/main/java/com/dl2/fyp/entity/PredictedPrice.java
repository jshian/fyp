package com.dl2.fyp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_predict_price")
@Table(indexes = @Index(name = "code_day",columnList = "code, day", unique = true))
public class PredictedPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Byte  day;

}
