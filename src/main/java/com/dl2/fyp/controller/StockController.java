package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.stock.RecommendationDto;
import com.dl2.fyp.dto.stock_event.StockEventDto;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.entity.User;
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

import java.security.Principal;
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
        if(recommendations != null && recommendations.size() > 0)
            return new ResponseEntity<Result>(
                    ResultUtil.success(recommendations)
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
