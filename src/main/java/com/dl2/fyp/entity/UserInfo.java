package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "t_user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal acceptableRisk;
    private Integer age;
    private Byte maritalStatus;
    private Byte familyNum;
    private Byte childNum;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private BigDecimal livingExpense;
    private BigDecimal housingExpense;
    private BigDecimal taxExpense;
    private BigDecimal miscelExpense;
    private BigDecimal expectedProfit;
    private BigDecimal expectedRisk;
    private String investmentGoal;
    private Short targetYears;
    private BigDecimal totalAsset;
    private BigDecimal debt;
    private BigDecimal debtRate;
    private BigDecimal equity;
    private BigDecimal cashFlow;
    private BigDecimal commission;
    private BigDecimal dividendCollectionFee;

}
