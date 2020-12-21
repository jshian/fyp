package com.dl2.fyp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity(name = "t_user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float acceptableRisk;
    @Range(min = 18,max = 100)
    private Integer age;
    private Byte maritalStatus;
//    private Byte familyNum;
//    private Byte childNum;
//    private Float monthlyIncome;
//    private Float monthlyExpense;
//    private Float livingExpense;
//    private Float housingExpense;
//    private Float taxExpense;
//    private Float miscelExpense;
//    private Float expectedProfit;
//    private Float expectedRisk;
//    private String investmentGoal;
//    private Short targetYears;
//    private Float totalAsset;
//    private Float debt;
//    private Float debtRate;
//    private Float equity;
//    private Float cashFlow;
//    private Float commission;
//    private Float dividendCollectionFee;

}
