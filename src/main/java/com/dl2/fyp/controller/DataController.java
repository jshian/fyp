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
    public ResponseEntity<Result> importStock(){
        Boolean success = stockService.importStocks();
        if (success)
        {
            return new ResponseEntity<Result>(
                    ResultUtil.success()
                    , HttpStatus.valueOf(200)
            );
        }
        return new ResponseEntity<Result>(
                ResultUtil.error(400,"Import fails")
                , HttpStatus.valueOf(400)
        );
    }

    @PutMapping("/stock/price")
    public ResponseEntity<Result> updateStockPrice(@RequestBody List<StockPriceInputDto> stockPriceInputDtoList){
        List<Stock> stockList = new ArrayList<>();
        for (StockPriceInputDto stockPriceInputDto : stockPriceInputDtoList) {
            Stock stock = stockService.getStockByCode(stockPriceInputDto.getCode());
            stock.setCurrentPrice(stockPriceInputDto.getPrice());
            stockList.add(stock);
        }
        Iterable<Stock> result = stockService.batchUpdateStock(stockList);
        if (result == null)
        {
            return new ResponseEntity<Result>(
                    ResultUtil.error(400,"update fails")
                    , HttpStatus.valueOf(400)
            );
        }
        return new ResponseEntity<Result>(
                ResultUtil.success()
                , HttpStatus.valueOf(200)
        );
    }

    @PutMapping("/stock/prediction")
    public ResponseEntity<Result> updateStockPrediction(){
        return new ResponseEntity<Result>(
                ResultUtil.success()
                , HttpStatus.valueOf(200)
        );
    }

    @PostMapping("/stock/news")
    public ResponseEntity<Result> addStockEvent(@RequestBody List<StockEventInputDto> stockEventDtoList){
        List<StockEvent> stockEventList = new ArrayList<>();
        for (StockEventInputDto stockEventInputDto : stockEventDtoList) {
            StockEvent stockEvent = new StockEvent();
            BeanUtils.copyProperties(stockEventInputDto, stockEvent);
            stockEvent.setStock(stockService.getStockByCode(stockEventInputDto.getCode()));
            stockEventList.add(stockEvent);
        }

        Iterable<StockEvent> result = stockService.addStockEvents(stockEventList);
        if (result == null)
        {
            return new ResponseEntity<Result>(
                    ResultUtil.error(400,"insert fails")
                    , HttpStatus.valueOf(400)
            );
        }
        return new ResponseEntity<Result>(
                ResultUtil.success()
                , HttpStatus.valueOf(200)
        );
    }
}
