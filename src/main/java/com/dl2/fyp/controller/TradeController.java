package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.service.account.TradeService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/account")
public class TradeController {
    private static Logger LOG = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    @PostMapping("/trade/Add")
    public Result addTrade(@RequestBody Map<String,String> jsonInput){
        if(jsonInput==null) return ResultUtil.error(-1, "invalid input");
        return tradeService.addTrade(jsonInput);
    }

    @PostMapping("/transaction/Add")
    public Result addTransaction(@RequestBody Map<String,String> jsonInput){
        if(jsonInput==null) return ResultUtil.error(-1, "invalid input");
        return tradeService.addTransaction(jsonInput);
    }
}
