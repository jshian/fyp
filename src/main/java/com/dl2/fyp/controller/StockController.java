package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.stock.PortfolioDto;
import com.dl2.fyp.dto.stock.PriceDto;
import com.dl2.fyp.dto.stock.RecommendationDto;
import com.dl2.fyp.dto.stock.StockPageDto;
import com.dl2.fyp.dto.stock_event.StockEventDto;
import com.dl2.fyp.entity.*;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.service.account.AccountService;
import com.dl2.fyp.service.risk.RiskService;
import com.dl2.fyp.service.stock.StockService;
import com.dl2.fyp.service.user.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/stock")
public class StockController {
    private static Logger LOG = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RiskService riskService;

    @GetMapping("/page/{code}")
    public ResponseEntity<Result> index(@PathVariable String code){
        if (code == null) return ResponseEntity.badRequest().body(ResultUtil.error(-1, "invalid input"));
        Stock stock = stockService.getStockByCode(code);
        if(stock==null || stock.getIsDelist())
            return ResponseEntity.badRequest().body(ResultUtil.error(-1,"failed to get"));
        List<StockEventDto> dtoList = new LinkedList<>();
        for(StockEvent stockEvent : stockService.getStockEvent(stock.getId())){
            dtoList.add(new StockEventDto(stockEvent));
        }
        dtoList.sort(Comparator.comparing(StockEventDto::getDatetime).reversed());
        List<String> labels = new LinkedList<>();
        List<PriceDto> historicalPriceList = new LinkedList<>();
        List<PriceDto> predictedPriceList = new LinkedList<>();
        for(HistoricalPrice historicalPrice : stockService.getHistoricalPriceByCode(code)){
            PriceDto price = new PriceDto(historicalPrice);
            PriceDto priceForPrediction = new PriceDto(historicalPrice);
            priceForPrediction.setY(null);
            labels.add(price.getX());
            historicalPriceList.add(price);
            predictedPriceList.add(priceForPrediction);
        }
        for(PredictedPrice predictedPrice : stockService.getPredictedPriceByCode(code)){
            PriceDto price = new PriceDto(predictedPrice);
            try{
                if (labels.size() == 0 || predictedPrice.getDate().after(new SimpleDateFormat("yyyy-MM-dd").parse(labels.get(labels.size()-1)))){
                    labels.add(price.getX());
                }
            }catch (Exception ex){
                labels.add(price.getX());
            }
            predictedPriceList.add(price);
        }

        StockPageDto stockPageDto = new StockPageDto(stock, dtoList, labels, historicalPriceList, predictedPriceList);
        return ResponseEntity.ok(ResultUtil.success(stockPageDto));
    }

    @GetMapping("/{code}")
    public Result getStockByCode(@PathVariable String code){
        if (code == null) return ResultUtil.error(-1, "invalid input");
        Stock stock = stockService.getStockByCode(code);
        if(stock==null)
            return ResultUtil.error(-1,"failed to get");
        return ResultUtil.success(stock);
    }

    @GetMapping("/GetAllEvent")
    public Result getStockEvent(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if (user == null) return ResultUtil.error(-1, "invalid input");
        List<StockInTrade> stockList = accountService.getAllStockInTrade(user);
        List<StockEventDto> dtoList = new LinkedList<>();
        for (StockInTrade stockInTrade : stockList){
            Map map = new HashMap<String, Object>();
            for(StockEvent stockEvent : stockService.getStockEvent(stockInTrade.getStock().getId())){
                dtoList.add(new StockEventDto(stockEvent));
            }
        }
        dtoList.sort(Comparator.comparing(StockEventDto::getCode));
        dtoList.sort(Comparator.comparing(StockEventDto::getDatetime).reversed());
        if(dtoList==null)
            return ResultUtil.error(-1,"failed to get");
        return ResultUtil.success(dtoList);
    }

    @GetMapping("/GetAll")
    public Result getAllStock(){
        return ResultUtil.success(stockService.getAllStock());
    }


    @GetMapping("/recommendation")
    public Result getStockRecommendation(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if (user == null) return ResultUtil.error(-1, "invalid input");
        List<Stock> stocks = stockService.getAllStock();
        return ResultUtil.success(stocks);
    }

    @GetMapping("/recommendation/portfolio")
    public ResponseEntity<Result> getStockPortfolio(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if (user == null)
            return new ResponseEntity<Result>(
                    ResultUtil.error(0,"Cannot find user")
                    , HttpStatus.valueOf(404)
            );
        List<RecommendationDto> recommendations = riskService.getRecommendationByUser(user, stockService.getAllStock());
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setPortfolio(recommendations);
        UserInfo userInfo = user.getUserInfo();
        portfolioDto.setMonthlyExpense(userInfo.getMonthlyExpense());
        portfolioDto.setUrgentSaving(userInfo.getMonthlyExpense().multiply(new BigDecimal(6)));
        portfolioDto.setInvestmentGoal(userInfo.getMonthlyExpense().multiply(new BigDecimal(300)));
        double count = 0;
        for (Account account : user.getAccountList()){
            if (account.getCategory() == AccountCategory.STOCK){
                double sum = 0;
                for(StockInTrade stockInTrade : account.getStockInTradesList()){
                    sum += stockInTrade.getStock().getCurrentPrice().doubleValue()*stockInTrade.getNumOfShare();
                }
                account.setAmount(new BigDecimal(sum));
            }
            count += account.getAmount().doubleValue();
        }
        portfolioDto.setTotalAsset(new BigDecimal(count));
        if (portfolioDto.getTotalAsset().compareTo(portfolioDto.getInvestmentGoal()) >=0){
            portfolioDto.setPortfolioType("Defensive");
        }else{
            portfolioDto.setPortfolioType("Aggressive");
        }
        if(recommendations != null && recommendations.size() > 0)
            return new ResponseEntity<Result>(
                    ResultUtil.success(portfolioDto)
                    , HttpStatus.valueOf(200)
            );
        else
            return new ResponseEntity<Result>(
                    ResultUtil.error(1,"Not enough cash")
                    , HttpStatus.valueOf(401)
            );

    }

    @GetMapping("/stockRisk/{code}")
    public Result getRiskFromStock(Principal principal, @PathVariable String code){
        User user = userService.findByFirebaseUid(principal.getName());
        if( user == null || code == null) return ResultUtil.error(-1, "invalid input");
        Stock stock = stockService.getStockByCode(code);
        if(stock==null) return ResultUtil.error(-1,"failed to get");
        else if(stock.getIsDelist()==true) return ResultUtil.error(-1,"the stock is delisted");
        return ResultUtil.success(riskService.calculateRiskFromStock(stock, user));
    }
}
