package com.dl2.fyp.service.risk;

import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.service.stock.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RiskService {
    private static Logger LOG = LoggerFactory.getLogger(RiskService.class);

    public BigDecimal calculateRiskFromUserInfo(UserInfo userInfo){
        return new BigDecimal(0);
    }
    public BigDecimal calculateRiskFromStock(Stock stock){
        return new BigDecimal(0);
    }
}
