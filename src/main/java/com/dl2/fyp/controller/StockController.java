package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.service.stock.StockService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {
    private static Logger LOG = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @PostMapping("/add_update")
    public Result addOrUpdateStock(Stock stock){
        if(stock==null) return ResultUtil.error(-1, "invalid input");
        if(stockService.addStock(stock)==null)
            return ResultUtil.error(-1,"failed to add or update");
        return ResultUtil.success("add or update stock");
    }

    @PostMapping("/event/add")
    public Result addStockEvent(StockEvent stockEvent){
        if(stockEvent==null) return ResultUtil.error(-1, "invalid input");
        if(stockService.addStockEvent(stockEvent)==null)
            return ResultUtil.error(-1,"failed to add");
        return ResultUtil.success("add stock event");
    }

    @GetMapping("/{code}")
    public Result getStockByCode(@PathVariable String code){
        if (code == null) return ResultUtil.error(-1, "invalid input");
        Stock stock = stockService.getStockByCode(code);
        if(stock==null)
            return ResultUtil.error(-1,"failed to get");
        return ResultUtil.success(stock);
    }

    @GetMapping("/event/{id}")
    public Result getStockEvent(@PathVariable Long id){
        if (id == null) return ResultUtil.error(-1, "invalid input");
        StockEvent stockEvent = stockService.getStockEvent(id);
        if(stockEvent==null)
            return ResultUtil.error(-1,"failed to get");
        return ResultUtil.success(stockEvent);
    }

    @GetMapping("/GetAll")
    public Result getAllStock(){
        return ResultUtil.success(stockService.getAllStock());
    }
}
