package com.dl2.fyp.service.risk;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.service.stock.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskService {
    private static Logger LOG = LoggerFactory.getLogger(RiskService.class);

    public BigDecimal calculateRiskFromUserInfo(UserInfo userInfo){
        return new BigDecimal(0);
    }
    public BigDecimal calculateRiskFromStock(Stock stock){
        return new BigDecimal(0);
    }

    public List<Stock> getRecommendationByUser(User user, List<Stock> stocks){
        List<Stock> recommendations = new ArrayList<>();
        Account stockAccount = user.getAccountList().stream().filter(o -> o.getCategory() == AccountCategory.STOCK).findFirst().orElse(null);
        List<Stock> stockInTradeList = new ArrayList<>();
        if (stockAccount != null){
            for (var stockInTrade: stockAccount.getStockInTradesList()){
                stockInTradeList.add(stockInTrade.getStock());
            }
        }
        for (Stock stock : stocks){
            if (!stockInTradeList.contains(stock))
                recommendations.add(stock);
        }
        return recommendations;
    }
}
