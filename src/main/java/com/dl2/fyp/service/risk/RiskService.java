package com.dl2.fyp.service.risk;

import com.dl2.fyp.entity.*;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RiskService {
    private static Logger LOG = LoggerFactory.getLogger(RiskService.class);

    @Autowired
    private StockInTradeRepository stockInTradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    public BigDecimal calculateRiskFromUserInfo(UserInfo userInfo){
        return new BigDecimal(0);
    }

    /**
     *
     * @param stock stock
     * @param user
     * @return -1 if the stock is delisted, return risk index otherwise
     */
    public BigDecimal calculateRiskFromStock(Stock stock, User user){
        BigDecimal A = user.getUserInfo().getAcceptableRisk();
        StockInTrade stockInTrade = null;
        for (Account account : user.getAccountList()) {
            stockInTrade = stockInTradeRepository.findByAccountIdAndStockId(account.getId(),stock.getId()).orElse(null);
            if(stockInTrade!=null) break;
        }
        BigDecimal investmentPrice = stockInTrade == null? stock.getCurrentPrice():stockInTrade.getAverageCost();
        // expected return = expected outcome / investment *100
        BigDecimal expectedReturn = stock.getExpectedOutcome().divide(investmentPrice).scaleByPowerOfTen(2);
        // upperStop return = upperStop / investment *100
        BigDecimal upperStopReturn = stock.getUpperStop().divide(investmentPrice).scaleByPowerOfTen(2);
        // downStop return = downStop / investment *100
        BigDecimal downStopReturn = stock.getDownStop().divide(investmentPrice).scaleByPowerOfTen(2);
        // return variance = accuracy * (upperStop return - expected return)^2 + (1 - accuracy)*(downStop return - expected return)^2
        BigDecimal returnVariance = stock.getAccuracy().multiply(upperStopReturn.subtract(expectedReturn))
                                    .add(BigDecimal.ONE.subtract(stock.getAccuracy()).multiply(downStopReturn.subtract(expectedReturn)));
        // Certainty Equivalent = expected return - 1/2*A*return variance
        return expectedReturn.subtract(returnVariance.multiply(A).multiply(BigDecimal.valueOf(0.5d)));
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

    public  List<Stock> getRecommendationByUser2(User user, List<Stock> stocks){
        List<Stock> recommendations = new LinkedList<>();
        Account stockAccount = accountRepository.findAccount(user.getId(),AccountCategory.STOCK).orElse(null);
        List<Stock> stockInTradeList = new LinkedList<>();
        if (stockAccount != null){
            stockInTradeList = stockInTradeRepository.findStockByAccount(stockAccount).orElse(null);
        }
        for (Stock stock : stocks){
            if (!stockInTradeList.contains(stock))
                recommendations.add(stock);
        }
        return recommendations;
    }
}
