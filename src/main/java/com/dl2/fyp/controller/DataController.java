package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.stock_event.StockEventInputDto;
import com.dl2.fyp.dto.stock.StockPriceInputDto;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.service.stock.StockService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stock/import")
    public Result importStock(){
        Boolean success = stockService.importStocks();
        if (success)
            return ResultUtil.error(HttpStatus.BAD_REQUEST, "import fails");
        return ResultUtil.success(HttpStatus.OK);
    }

    @PutMapping("/stock/price")
    public Result updateStockPrice(@RequestBody List<StockPriceInputDto> stockPriceInputDtoList){
        List<Stock> stockList = new ArrayList<>();
        for (StockPriceInputDto stockPriceInputDto : stockPriceInputDtoList) {
            Stock stock = stockService.getStockByCode(stockPriceInputDto.getCode());
            stock.setCurrentPrice(stockPriceInputDto.getPrice());
            stockList.add(stock);
        }
        Iterable<Stock> result = stockService.batchUpdateStock(stockList);
        if (result == null)
            return ResultUtil.error(HttpStatus.BAD_REQUEST, "update fails");
        return ResultUtil.success(HttpStatus.OK);
    }

    @PutMapping("/stock/prediction")
    public Result updateStockPrediction(){
        return ResultUtil.success(HttpStatus.OK);
    }

    @PostMapping("/stock/news")
    public Result addStockEvent(@RequestBody List<StockEventInputDto> stockEventDtoList){
        List<StockEvent> stockEventList = new ArrayList<>();
        for (StockEventInputDto stockEventInputDto : stockEventDtoList) {
            StockEvent stockEvent = new StockEvent();
            BeanUtils.copyProperties(stockEventInputDto, stockEvent);
            stockEvent.setStock(stockService.getStockByCode(stockEventInputDto.getCode()));
            stockEventList.add(stockEvent);
        }

        Iterable<StockEvent> result = stockService.addStockEvents(stockEventList);
        if (result == null)
            return ResultUtil.error(HttpStatus.BAD_REQUEST,"insert fails");
        return ResultUtil.success(HttpStatus.OK);

    }
}
