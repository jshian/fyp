package com.dl2.fyp.service.stock;

import com.dl2.fyp.entity.HistoricalPrice;
import com.dl2.fyp.entity.PredictedPrice;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import com.dl2.fyp.repository.stock.HistoricalPriceRepository;
import com.dl2.fyp.repository.stock.PredictedPriceRepository;
import com.dl2.fyp.repository.stock.StockEventRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {
    private static Logger LOG = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private HistoricalPriceRepository historicalPriceRepository;
    @Autowired
    private PredictedPriceRepository predictedPriceRepository;
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

    public List<HistoricalPrice> getHistoricalPriceByCode(String code) {
        return historicalPriceRepository.getHistoricalPriceByCode(code);
    }

    public List<PredictedPrice> getPredictedPriceByCode(String code) {
        return predictedPriceRepository.getPredictedPriceByCode(code);
    }

    public List<String> getAllCode(){
        return stockRepository.getAllCode();
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
            if (!stock.getIsDelist())
                stocks.add(stock);
        }
        return stocks;
    }

    @Transactional
    public boolean refreshPrediction(String date){
        try{
            stockRepository.clearSellOutDate();
            for (Stock stock : stockRepository.findAll()) {
                if (!stock.getIsDelist()){
                    BigDecimal expectedReturn = stock.getUpperStop().divide(stock.getCurrentPrice(),2, RoundingMode.HALF_UP);
                    // upperStop return = upperStop / investment *100
                    BigDecimal upperStopReturn = stock.getUpperStop().divide(stock.getCurrentPrice(),2, RoundingMode.HALF_UP);
                    // downStop return = downStop / investment *100
                    BigDecimal downStopReturn = stock.getDownStop().divide(stock.getCurrentPrice(),2, RoundingMode.HALF_UP);
                    // return variance = accuracy * (upperStop return - expected return)^2 + (1 - accuracy)*(downStop return - expected return)^2
                    BigDecimal riskIndex = (stock.getAccuracy().multiply((upperStopReturn.subtract(expectedReturn)).pow(2))
                            .add(BigDecimal.ONE.subtract(stock.getAccuracy()).multiply((downStopReturn.subtract(expectedReturn)).pow(2)))).sqrt(new MathContext(2, RoundingMode.DOWN));

                    int holdingPeriod = 0;
                    if (stock.getDownStop().compareTo(stock.getCurrentPrice())<0 && stock.getUpperStop().compareTo(stock.getCurrentPrice())>0){
                        List<PredictedPrice> prices = predictedPriceRepository.getPredictedPriceByCode(stock.getCode());
                        for (PredictedPrice price : prices){
                            if(price.getPrice().compareTo(stock.getDownStop()) <= 0){
                                break;
                            }else if (price.getPrice().compareTo(stock.getUpperStop()) > -1){
                                if(price.getDate() != null && price.getDate().after(new SimpleDateFormat( "yyyyMMdd" ).parse(date))){
                                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
                                    holdingPeriod = (int) Math.abs(ChronoUnit.DAYS.between(
                                            Instant.ofEpochMilli(price.getDate().getTime())
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate()
                                            , localDate));
                                    stock.setSellOutDate(price.getDate());
                                    break;
                                }
                            }
                        }
                    }
                    stock.setHoldingPeriod(holdingPeriod);
                    stock.setRiskIndex(riskIndex);
                    stockRepository.save(stock);
                }
            }
            return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
