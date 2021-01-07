package com.dl2.fyp.dto.stock;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPriceInputDto {
    private String code;
    private BigDecimal price;
}
