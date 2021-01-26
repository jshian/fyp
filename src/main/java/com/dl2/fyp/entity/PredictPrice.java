package com.dl2.fyp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_predict_price")
public class PredictPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;
    @NotNull
    private Date date;
    @NotNull
    private BigDecimal price;

}
