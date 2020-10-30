package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "t_user_info")
public class UserInfo {

    private Float acceptableRisk;
    @Range(min = 18,max = 100)
    private Integer age;
    private Byte maritalStatus;
    private Byte familyNum;
    private Byte childNum;
    private Integer monthlyIncome;
    private Integer monthlyExpense;
    private Integer livingExpense;
    private Integer housingExpense;
    private Integer taxExpense;
    private Integer miscelExpense;
    private Integer expectedProfit;
    private Integer expectedRisk;
    @Column(name = "goal", columnDefinition = "text")
    private String investmentGoal;
    private Short targetYears;
    private Integer totalAsset;
    private Integer debt;
    private Integer debtRate;
    private Integer equity;
    private Integer cashFlow;
    private Integer commission;
    private Integer dividend;

}
