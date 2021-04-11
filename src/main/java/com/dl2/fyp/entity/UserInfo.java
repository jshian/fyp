package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Range(min = 0, max = 4, message = "Out of range")
    private BigDecimal acceptableRisk;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Past
    private Date dateOfBirth;
    @NotNull
    private Boolean maritalStatus;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Byte familyNum;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private Byte childNum;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal monthlyIncome;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal monthlyExpense;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal livingExpense;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal housingExpense;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal taxExpense;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal miscelExpense;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal investmentGoal;
    @Min(value = 0, message = "invalid negative input")
    private Short targetYears;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal totalAsset;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal debt;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal debtRate;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal equity;
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal cashFlow;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal commission;
    @NotNull
    @Min(value = 0, message = "invalid negative input")
    private BigDecimal dividendCollectionFee;

}
