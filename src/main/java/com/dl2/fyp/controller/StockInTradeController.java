package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.service.account.StockInTradeService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/trading_stock")
public class StockInTradeController {
    private static Logger LOG = LoggerFactory.getLogger(StockInTradeController.class);

    @Autowired
    private StockInTradeService stockInTradeService;

    @PostMapping("/add/")
    public Result addStockInTrade(@RequestBody StockInTrade stockInTrade){
        if(stockInTrade==null) return ResultUtil.error(-1, "invalid input");
        if(stockInTradeService.addStockInTrade(stockInTrade)==null)
            return ResultUtil.error(-1,"failed to add");
        return ResultUtil.success("added trading stock");
    }
}
