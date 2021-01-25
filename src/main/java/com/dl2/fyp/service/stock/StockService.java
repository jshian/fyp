package com.dl2.fyp.service.stock;

import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.repository.stock.StockEventRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {
    private static Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockEventRepository stockEventRepository;

    @Value("${stock.list-url}")
    private String stockListUrl;

    public Boolean importStocks()  {
        try
        {
            if(stockRepository.count() == 0){
                InputStream json = new URL(stockListUrl).openStream();
                final ObjectMapper objectMapper = new ObjectMapper();
                List<Stock> stockList = objectMapper.readValue(json,new TypeReference<>() {});
                for (Stock stock: stockList) {
                    if (stockRepository.findByCode(stock.getCode()) != null) return null;
                }
                stockRepository.saveAll(stockList);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public Iterable<Stock>  batchUpdateStock(List<Stock> stockList){
        Iterable<Stock> result;
        try {
            result = stockRepository.saveAll(stockList);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    @Transactional
    public Stock addStock(Stock stock){
        LOG.debug("add stock, param={}",stock);
        if (stockRepository.existsById(stock.getId())) return null;
        Stock result;
        try {
            result = stockRepository.save(stock);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    @Transactional
    public Iterable<StockEvent> addStockEvents(List<StockEvent> stockEventList){
        Iterable<StockEvent>  result;
        try {
            result = stockEventRepository.saveAll(stockEventList);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public Stock getStockByCode(String code){
        LOG.debug("get stock, code={}",code);
        Stock result = stockRepository.findByCode(code);
        LOG.debug("get stock, result={}",result);
        return result;
    }

    public List<StockEvent> getStockEvent(Long id){
        LOG.debug("get stock event, id={}",id);
        List<StockEvent> result = stockEventRepository.findByStockId(id).orElse(null);
        LOG.debug("get stock event, result={}",result);
        return result.stream()
                .filter(
                        o -> o.getDatetime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                                .isAfter(LocalDate.now(ZoneId.systemDefault()).minusDays(7)))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllStockDto(){
        List list = new LinkedList();
        for (Stock stock : stockRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",stock.getId());
            map.put("code",stock.getCode());
            list.add(map);
        }
        return list;
    }

    public List<Stock> getAllStock(){
        List<Stock> stocks = new ArrayList<>();
        for (Stock stock : stockRepository.findAll()) {
            stocks.add(stock);
        }
        return stocks;
    }

    public List<Stock> getStockByKeywordAndPaging(String keyword, int pageNumber,int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(Sort.Direction.ASC,"code"));
        return stockRepository.findStocksByKeyword(keyword, pageable);
    }

    public List<StockEvent> getStockEventByKeywordAndPaging(String keyword, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize,Sort.by(Sort.Direction.DESC,"datetime"));
        return stockEventRepository.findStockEventsByKeyword(keyword, pageable);
    }

}
