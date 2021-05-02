package com.dl2.fyp.dto.stock;

import com.dl2.fyp.entity.HistoricalPrice;
import com.dl2.fyp.entity.PredictedPrice;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
public class PriceDto {
    private String x;
    private BigDecimal y;
    public PriceDto(HistoricalPrice historicalPrice){
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        x =  df.format(historicalPrice.getDate());
        y = historicalPrice.getClose();
    }
    public PriceDto(PredictedPrice predictedPrice){
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        x =  df.format(predictedPrice.getDate());
        y = predictedPrice.getPrice();
    }
}
