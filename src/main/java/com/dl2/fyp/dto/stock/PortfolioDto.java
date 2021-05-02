package com.dl2.fyp.dto.stock;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioDto {
    private List<RecommendationDto> portfolio;
    private BigDecimal totalAsset;
    private BigDecimal investmentGoal;
    private BigDecimal monthlyExpense;
    private String portfolioType;
    private BigDecimal urgentSaving;
}
