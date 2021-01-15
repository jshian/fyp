package com.dl2.fyp.dto.trade;

import com.dl2.fyp.entity.Trade;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeInputDto extends Trade {
    private Long stockId;
}
