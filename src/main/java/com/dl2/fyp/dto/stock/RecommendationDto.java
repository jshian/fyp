package com.dl2.fyp.dto.stock;

import com.dl2.fyp.entity.Stock;
import lombok.Data;

@Data
public class RecommendationDto {
    private int id;
    private Stock stock;
    private double proportion;
    private double noOfShare;
    private double cashEquivalent;
    private String type;
    private int mappedStock = 0;
}
