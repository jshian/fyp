package com.dl2.fyp.service.stock;

import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.repository.stock.StockEventRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class StockService {
    private static Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @Transactional
    public Stock addStock(Stock stock){
        LOG.debug("add stock, param={}",stock);
        if (stockRepository.existsById(stock.getId())) return null;
        Stock result;
        try {
            result = stockRepository.save(stock);
        } catch (IllegalArgumentException e) {
            return null;
        }
        LOG.debug("add stock, result={}",result);
        return result;
    }

    @Transactional
    public StockEvent addStockEvent(StockEvent stockEvent){
        LOG.debug("add stock event, param={}",stockEvent);
        StockEvent result;
        try {
            result = stockEventRepository.save(stockEvent);
        } catch (IllegalArgumentException e) {
            return null;
        }
        LOG.debug("add stock event, result={}",result);
        return result;
    }

    public Stock getStockByCode(String code){
        LOG.debug("get stock, code={}",code);
        Stock result = stockRepository.findByCode(code).orElse(null);
        LOG.debug("get stock, result={}",result);
        return result;
    }

    public StockEvent getStockEvent(Long id){
        LOG.debug("get stock event, id={}",id);
        Stock stock = stockRepository.findById(id).orElse(null);
        StockEvent result = stockEventRepository.findByStock(stock).orElse(null);
        LOG.debug("get stock event, result={}",result);
        return result;
    }

    public List<Map<String, Object>> getAllStock(){
        List list = new LinkedList();
        for (Stock stock : stockRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("stockId",stock.getId());
            map.put("stockSymbol",stock.getCode());
            list.add(map);
        }
        return list;
    }

}
