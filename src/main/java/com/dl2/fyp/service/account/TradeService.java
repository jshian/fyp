package com.dl2.fyp.service.account;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.*;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import com.dl2.fyp.repository.account.TradeRepository;
import com.dl2.fyp.repository.account.TransactionRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TradeService {
    private static Logger LOG = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private StockInTradeRepository stockInTradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public Result addTrade(Map<String, String> jsonInput){
        LOG.debug("add stock in trade, param={}",jsonInput);
        // get the json input
        Account cashAccount = accountRepository.findAccount(Long.valueOf(jsonInput.get("userId")), AccountCategory.CASH).orElse(null);
        Account stockAccount = accountRepository.findAccount(Long.valueOf(jsonInput.get("userId")), AccountCategory.STOCK).orElse(null);
        Stock stock = stockRepository.findById(Long.valueOf(jsonInput.get("stockId"))).orElse(null);
        Boolean action = Boolean.valueOf(jsonInput.get("action"));
        BigDecimal price = new BigDecimal(jsonInput.get("price"));
        Long numOfShare = Long.valueOf(jsonInput.get("numOfShare"));
        StockInTrade stockInTrade = stockInTradeRepository.findByAccountIdAndStockId(stockAccount.getId(),stock.getId()).orElse(null);

        // check input
        if (stockAccount==null||cashAccount==null) return ResultUtil.error(404,"account not founded");
        else if (stock==null) return ResultUtil.error(404,"stock not founded");
        else if (stockInTrade==null){
            if (action==true){
                stockInTrade = new StockInTrade();
                stockInTrade.setAccount(stockAccount);
                stockInTrade.setStock(stock);
                stockInTrade.setNumOfShare(0L);
                stockInTrade.setAverageCost(BigDecimal.ZERO);
            }else
            return ResultUtil.error(400, "have not hold the stock yet");
        }else if (action==true && cashAccount.getAmount().compareTo(price.multiply(new BigDecimal(numOfShare)))<1)
            return ResultUtil.error(400,"not enough cash in Cash Account");
        else if (action==false && stockInTrade.getNumOfShare()<numOfShare){
            return ResultUtil.error(400,"not enough numOfShare in StockInTrade");
        }
        try {
            //set trade
            Trade trade = new Trade();
            trade.setAction(action);
            trade.setNumOfShare(numOfShare);
            trade.setPrice(price);

            //set stock in trade
            if (action==true){
                trade.setProfit(BigDecimal.ZERO);
                trade.setProfitPercentage(BigDecimal.ZERO);
                BigDecimal newAverageCost = stockInTrade.getAverageCost().add(price.multiply(BigDecimal.valueOf(numOfShare)))
                        .divide(BigDecimal.valueOf(numOfShare).add(BigDecimal.valueOf(stockInTrade.getNumOfShare())));
                stockInTrade.setNumOfShare(numOfShare+stockInTrade.getNumOfShare());
                stockInTrade.setAverageCost(newAverageCost);
                trade.setCostAfter(newAverageCost);

                addTransaction(stockAccount,cashAccount,price.multiply(BigDecimal.valueOf(numOfShare)));
            }else{
                BigDecimal profit = price.subtract(stockInTrade.getAverageCost()).multiply(BigDecimal.valueOf(numOfShare));
                trade.setProfit(profit);
                trade.setProfitPercentage(profit.divide(price.divide(stockInTrade.getAverageCost())));
                stockInTrade.setNumOfShare(stockInTrade.getNumOfShare()-numOfShare);

                addTransaction(cashAccount,stockAccount,price.multiply(BigDecimal.valueOf(numOfShare)));
            }
            stockInTrade.getTradeList().add(trade);
            stockInTradeRepository.save(stockInTrade);
        }catch (IllegalArgumentException e){
            return ResultUtil.error(404, "failed to trade");
        }
        LOG.debug("add stock in trade, result={}",stockInTrade);
        return ResultUtil.success("added trading stock");
    }

    public Result addTransaction(Map<String, String> jsonInput) {
        LOG.debug("add transaction, param={}",jsonInput);
        // get the json input
        Account accountIn = accountRepository.findById(Long.valueOf(jsonInput.get("accountIn"))).orElse(null);
        Account accountOut = accountRepository.findById(Long.valueOf(jsonInput.get("accountOut"))).orElse(null);
        BigDecimal amount = new BigDecimal(jsonInput.get("amount"));
        Result result = addTransaction(accountIn,accountOut,amount);
        LOG.debug("add transaction, result={}",result);
        return result;
    }

    @Transactional
    public Result addTransaction(Account accountIn, Account accountOut, BigDecimal amount) {
        if (accountIn==null||accountOut==null) return ResultUtil.error(404,"account not founded");
        else if (amount.compareTo(BigDecimal.ZERO)<1) return ResultUtil.error(404, "require valid transaction amount");
        else if (accountOut.getAmount().compareTo(amount)<1) return ResultUtil.error(400, "not enough cash in account");
        Transaction transaction = new Transaction();
        try {
            transaction.setAccountIn(accountIn);
            transaction.setAccountOut(accountOut);
            transaction.setAmount(amount);
            BigDecimal accountInAfter = accountIn.getAmount().add(amount);
            transaction.setAccountInAmountAfter(accountInAfter);
            accountIn.setAmount(accountInAfter);
            accountRepository.save(accountIn);
            BigDecimal accountOutAfter = accountIn.getAmount().subtract(amount);
            accountOut.setAmount(accountOutAfter);
            accountRepository.save(accountOut);
            transaction.setAccountOutAmountAfter(accountOutAfter);
            transactionRepository.save(transaction);
        }catch (IllegalArgumentException e){
            return ResultUtil.error(404, "failed to trade");
        }

        return ResultUtil.success("added transaction");
    }
}
