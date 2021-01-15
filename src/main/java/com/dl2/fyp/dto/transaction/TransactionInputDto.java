package com.dl2.fyp.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionInputDto {
    private Long accountInId;
    private Long accountOutId;
    private BigDecimal amount;
}
