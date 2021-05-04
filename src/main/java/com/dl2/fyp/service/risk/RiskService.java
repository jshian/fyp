package com.dl2.fyp.service.risk;

import com.dl2.fyp.dto.stock.RecommendationDto;
import com.dl2.fyp.entity.*;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import com.dl2.fyp.repository.stock.PredictedPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiskService {
    private static Logger LOG = LoggerFactory.getLogger(RiskService.class);

    @Autowired
    private StockInTradeRepository stockInTradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${fire.retired-growth}")
    private double retiredGrowth;

    @Value("${fire.market-growth}")
    private double marketGrowth;

    @Value("${fire.yearly-return}")
    private double yearlyReturn;

    public BigDecimal calculateRiskFromUserInfo(User user, UserInfo userInfo){
        double count = 0;
        for (Account account : user.getAccountList()){
            count += account.getAmount().doubleValue();
        }
        double progress = count / (userInfo.getMonthlyExpense().doubleValue() * 12 / yearlyReturn);
        if(progress > 1){
            progress = 1;
        }
        return new BigDecimal(Math.pow(progress,2)*4);
    }

    /**
     *
     * @param stock stock
     * @param user
     * @return -1 if the stock is delisted, return risk index otherwise
     */
    public BigDecimal calculateRiskFromStock(Stock stock, User user){
        BigDecimal A = calculateRiskFromUserInfo(user, user.getUserInfo());

        BigDecimal expectedReturn = stock.getUpperStop().divide(stock.getCurrentPrice(), 2, RoundingMode.HALF_UP);
        // Certainty Equivalent = expected return - 1/2*A*return variance
        return expectedReturn.subtract(stock.getRiskIndex().pow(2).multiply(A).multiply(BigDecimal.valueOf(0.5d)));
    }

    public List<RecommendationDto> getRecommendationByUser(User user, List<Stock> stocks){
        List<Stock> profitStocks = new ArrayList<>();
        List<Stock> balancedStocks = new ArrayList<>();
        Account stockAccount = user.getAccountList().stream().filter(o -> o.getCategory() == AccountCategory.STOCK).findFirst().orElse(null);
        List<Stock> stockInTradeList = new ArrayList<>();
        if (stockAccount != null){
            for (var stockInTrade: stockAccount.getStockInTradesList()){
                stockInTradeList.add(stockInTrade.getStock());
            }
        }
        Boolean isRetired = true;

        double totalCashAmt = 0;
        for (Account account : user.getAccountList()){
            totalCashAmt += account.getAmount().doubleValue();
        }
        for (Stock stock : stocks){
            if (!stockInTradeList.contains(stock) && stock.getHoldingPeriod() > 0){
                BigDecimal ce = calculateRiskFromStock(stock, user);
                UserInfo userInfo = user.getUserInfo();
                double basicGrowth = Math.pow((1.0001853),stock.getHoldingPeriod())-1;
                double expectedGrowth = basicGrowth;
                if(new BigDecimal(totalCashAmt).compareTo(userInfo.getMonthlyExpense().multiply(new BigDecimal(12)).divide(new BigDecimal(yearlyReturn),2,RoundingMode.HALF_UP)) < 0){
                    expectedGrowth = Math.pow((1.0003445),stock.getHoldingPeriod())-1;
                    isRetired = false;
                }
                if(ce.doubleValue() >= expectedGrowth){
                    profitStocks.add(stock);
                    balancedStocks.add(stock);
                }else if (ce.doubleValue() >= basicGrowth){
                    balancedStocks.add(stock);
                }
            }
        }

        double A = calculateRiskFromUserInfo(user, user.getUserInfo()).doubleValue();
        if(A/4 > 0.5){
            Collections.sort(profitStocks, Comparator.comparing(Stock::getUpperStop).reversed());
        }else{
            Collections.sort(profitStocks, Comparator.comparing(Stock::getRiskIndex).reversed());
        }
        Collections.sort(balancedStocks, Comparator.comparing(Stock::getRiskIndex));

        profitStocks = profitStocks.stream().limit(5).collect(Collectors.toList());
        balancedStocks = balancedStocks.stream().limit(10).collect(Collectors.toList());

        System.out.println(profitStocks.size());
        System.out.println(balancedStocks.size());

        Account cashAccount = user.getAccountList().stream().takeWhile(account -> account.getCategory() == AccountCategory.CASH).findFirst().orElse(null);
        if (cashAccount == null || cashAccount.getAmount().doubleValue() <= 0){
            return null;
        }
        int count = 0;
        double cashLeft = totalCashAmt;
        List<RecommendationDto> recommendationDtos = new ArrayList<>();
        while (count < 10 || cashLeft > 0){
            if(isRetired){
                for (Stock balancedStock : balancedStocks) {
                    RecommendationDto recommendation = getRecommendationFromProportion(balancedStock, 1.0/balancedStocks.size(), cashLeft, totalCashAmt);
                    recommendation.setId(count);
                    recommendation.setType("Balanced");
                    recommendationDtos.add(recommendation);
                    count += 1;
                }
                return recommendationDtos;
            }
            if (balancedStocks.size() == 0 || profitStocks.size() == 0){
                break;
            }
            Stock profitStock = profitStocks.get(0);
            Stock balancedStock  = balancedStocks.get(0);
            profitStocks.remove(profitStock);
            balancedStocks.remove(balancedStock);

            double profitSize = getProfitSize(A, profitStock, balancedStock);

            RecommendationDto recommendation = getRecommendationFromProportion(profitStock, profitSize, cashLeft, totalCashAmt);
            recommendation.setId(count);
            recommendation.setType("Profit");
            recommendation.setMappedStock(count + 1);
            final RecommendationDto finalRecommendation = recommendation;
            RecommendationDto existingRecommendation = recommendationDtos.stream().filter(recommendationDto -> recommendationDto.getStock().getCode() == finalRecommendation.getStock().getCode()).findFirst().orElse(null);
            if (existingRecommendation != null){
                existingRecommendation.setProportion(recommendation.getProportion() + existingRecommendation.getProportion());
                existingRecommendation.setNoOfShare(recommendation.getNoOfShare() + existingRecommendation.getNoOfShare());
                existingRecommendation.setCashEquivalent(recommendation.getCashEquivalent() + existingRecommendation.getCashEquivalent());
            }else{
                if(recommendation.getNoOfShare() > 0){
                    recommendationDtos.add(recommendation);
                }
            }
            count += 1;

            recommendation = getRecommendationFromProportion(balancedStock, 0.2-profitSize, cashLeft, totalCashAmt);
            recommendation.setId(count);
            recommendation.setType("Balanced");
            recommendation.setMappedStock(count - 1);
            final RecommendationDto finalRecommendation2 = recommendation;
            existingRecommendation = recommendationDtos.stream().filter(recommendationDto -> recommendationDto.getStock().getCode() == finalRecommendation2.getStock().getCode()).findFirst().orElse(null);
            if (existingRecommendation != null){
                existingRecommendation.setProportion(recommendation.getProportion() + existingRecommendation.getProportion());
                existingRecommendation.setNoOfShare(recommendation.getNoOfShare() + existingRecommendation.getNoOfShare());
                existingRecommendation.setCashEquivalent(recommendation.getCashEquivalent() + existingRecommendation.getCashEquivalent());
            }else{
                if(recommendation.getNoOfShare() > 0){
                    recommendationDtos.add(recommendation);
                }
            }
            count += 1;
        }

        return recommendationDtos;
    }

    private double getProfitSize(double A, Stock profitStock, Stock balancedStock){
        double profitSize = 0.2;
        if (A/4 > 0.5){
            if(profitStock.getRiskIndex().compareTo(balancedStock.getRiskIndex()) >= 0)
                profitSize *= A/4;
            else
                profitSize *= 1-A/4;
        }else {
            profitSize *= 1 - A / 4;
        }
        return profitSize;
    }

    private RecommendationDto getRecommendationFromProportion(Stock stock, double proportion, double cashLeft, double totalCashAmt)
    {
        RecommendationDto recommendationDto = new RecommendationDto();
        recommendationDto.setStock(stock);
        int numOfShare = (int)Math.floor(cashLeft*proportion/stock.getCurrentPrice().doubleValue());
        recommendationDto.setCashEquivalent(numOfShare*stock.getCurrentPrice().doubleValue());
        recommendationDto.setNoOfShare(numOfShare);
        recommendationDto.setProportion(recommendationDto.getCashEquivalent()/totalCashAmt);
        return recommendationDto;
    }
}
