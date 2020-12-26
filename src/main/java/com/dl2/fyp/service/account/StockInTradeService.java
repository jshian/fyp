package com.dl2.fyp.service.account;

import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import com.dl2.fyp.repository.account.TradeRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class StockInTradeService {
    private static Logger LOG = LoggerFactory.getLogger(StockInTradeService.class);

    @Autowired
    private StockInTradeRepository stockInTradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Transactional
    public StockInTrade addStockInTrade(StockInTrade stockInTrade){
        LOG.debug("add stock in trade, param={}",stockInTrade);
        // check parameter
        if (accountRepository.existsById(stockInTrade.getAccount().getId())
            || stockRepository.existsById(stockInTrade.getStock().getId())
            || stockInTrade.getAverageCost().compareTo(BigDecimal.ZERO)==1||stockInTrade.getNumOfShare()>0)
            return null;
        try {
            stockInTradeRepository.save(stockInTrade);
        }catch (IllegalArgumentException e){
            return null;
        }
        LOG.debug("add stock in trade, result={}",stockInTrade);
        return stockInTrade;
    }
}
