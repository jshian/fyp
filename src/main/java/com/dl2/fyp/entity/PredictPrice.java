package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_predict_price")
public class PredictPrice {
    @Column(name = "code", length = 6)
    @NotNull
    private String code;
    @NotNull
    private Date date;
    @NotNull
    private BigDecimal price;

}
