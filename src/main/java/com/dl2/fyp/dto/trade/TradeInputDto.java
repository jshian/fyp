package com.dl2.fyp.dto.trade;

import com.dl2.fyp.entity.Trade;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class TradeInputDto extends Trade {
    private Long stockId;
}
