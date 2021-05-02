package com.dl2.fyp.service.account;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.stock_event.StockEventDto;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public Result addTrade(User user, Trade trade, Long stockId ){
        LOG.debug("add stock in trade, param={}",trade);
        // get the json input
        Account cashAccount = user.getAccountList().stream().filter(o -> o.getCategory() == AccountCategory.CASH).findFirst().orElse(null);
        Account stockAccount = user.getAccountList().stream().filter(o -> o.getCategory() == AccountCategory.STOCK).findFirst().orElse(null);
        Stock stock = stockRepository.findById(stockId).orElse(null);
        Boolean action = trade.getAction();
        BigDecimal price = trade.getPrice();
        Long numOfShare = trade.getNumOfShare();
        StockInTrade stockInTrade = stockInTradeRepository.findByAccountIdAndStockId(stockAccount.getId(),stock.getId()).orElse(null);

        // check input
        if (stockAccount==null||cashAccount==null) return ResultUtil.error(404,"account not founded");
        if (stock==null) return ResultUtil.error(404,"stock not founded");
        if (stockInTrade==null){
            if (action==true){
                stockInTrade = new StockInTrade();
                stockInTrade.setAccount(stockAccount);
                stockInTrade.setStock(stock);
                stockInTrade.setNumOfShare(0L);
                stockInTrade.setAverageCost(BigDecimal.ZERO);
            }else
                return ResultUtil.error(400, "have not hold the stock yet");
        }
        if (action==true && cashAccount.getAmount().compareTo(price.multiply(new BigDecimal(numOfShare)))<1)
            return ResultUtil.error(400,"not enough cash in Cash Account");
        if (action==false && stockInTrade.getNumOfShare()<numOfShare){
            return ResultUtil.error(400,"not enough numOfShare in StockInTrade");
        }
        try {
            //set stock in trade
            if (action==true){
                trade.setProfit(BigDecimal.ZERO);
                trade.setProfitPercentage(BigDecimal.ZERO);
                BigDecimal newAverageCost =
                        stockInTrade.getAverageCost()
                        .multiply(BigDecimal.valueOf(stockInTrade.getNumOfShare()))
                        .add(price.multiply(BigDecimal.valueOf(numOfShare)))
                        .divide(BigDecimal.valueOf(stockInTrade.getNumOfShare()+numOfShare),2, RoundingMode.HALF_UP)
                        ;
                stockInTrade.setNumOfShare(numOfShare+stockInTrade.getNumOfShare());
                stockInTrade.setAverageCost(newAverageCost);
                trade.setCostAfter(newAverageCost);
                trade.setTotalShareAfter(stockInTrade.getNumOfShare());

                addTransaction(stockAccount,cashAccount,price.multiply(BigDecimal.valueOf(numOfShare)));
            }else{
                BigDecimal profit = price.subtract(stockInTrade.getAverageCost()).multiply(BigDecimal.valueOf(numOfShare));
                trade.setProfit(profit);
                trade.setProfitPercentage(price.subtract(stockInTrade.getAverageCost()).divide(stockInTrade.getAverageCost(), 2, RoundingMode.HALF_UP));
                if(stockInTrade.getNumOfShare() > numOfShare)
                    trade.setCostAfter(stockInTrade.getAverageCost());
                else
                    trade.setCostAfter(new BigDecimal(0));
                stockInTrade.setAverageCost(trade.getCostAfter());
                stockInTrade.setNumOfShare(stockInTrade.getNumOfShare()-numOfShare);
                trade.setTotalShareAfter(stockInTrade.getNumOfShare());

                addTransaction(cashAccount,stockAccount,price.multiply(BigDecimal.valueOf(numOfShare)));
            }
            stockInTrade.getTradeList().add(trade);
            stockInTradeRepository.save(stockInTrade);
        }catch (Exception e){
            return ResultUtil.error(404, "failed to trade");
        }
        LOG.debug("add stock in trade, result={}",stockInTrade);
        return ResultUtil.success("added trading stock");
    }

    @Transactional
    public Result addTransaction(Account accountIn, Account accountOut, BigDecimal amount) {
        if (accountIn==null||accountOut==null) return ResultUtil.error(404,"account not founded");
        else if (amount.compareTo(BigDecimal.ZERO)<1) return ResultUtil.error(404, "require valid transaction amount");
        else if (accountOut.getAmount().compareTo(amount)<1 && accountOut.getCategory() != AccountCategory.STOCK) return ResultUtil.error(400, "not enough cash in account");
        Transaction transaction = new Transaction();
        try {
            transaction.setAccountIn(accountIn);
            transaction.setAccountOut(accountOut);
            transaction.setAmount(amount);
            BigDecimal accountInAfter = accountIn.getAmount().add(amount);
            transaction.setAccountInAmountAfter(accountInAfter);
            accountIn.setAmount(accountInAfter);
            accountRepository.save(accountIn);
            BigDecimal accountOutAfter = new BigDecimal(0);
            if(accountOut.getCategory() == AccountCategory.STOCK){
                for (StockInTrade stockInTrade :accountOut.getStockInTradesList()) {
                    accountOutAfter = accountOutAfter.add(stockInTrade.getStock().getCurrentPrice().multiply(BigDecimal.valueOf(stockInTrade.getNumOfShare())));
                }
            }else{
                accountOutAfter = accountOut.getAmount().subtract(amount);
            }
            accountOut.setAmount(accountOutAfter);
            accountRepository.save(accountOut);
            transaction.setAccountOutAmountAfter(accountOutAfter);
            transactionRepository.save(transaction);
        }catch (Exception e){
            return ResultUtil.error(404, "failed to trade");
        }

        return ResultUtil.success("added transaction");
    }

    public List<Trade> getTradeByStockInTradeId(User user, Long stockInTradeId, Long days){
        Account stockAccount = user.getAccountList().stream()
                .filter(o -> o.getCategory() == AccountCategory.STOCK)
                .findFirst().orElse(null);
        if(stockAccount==null)
            return null;
        StockInTrade stockInTrade = stockAccount.getStockInTradesList().stream()
                .filter(o -> o.getId() == stockInTradeId)
                .findFirst().orElse(null);
        if(stockInTrade==null)
            return null;
        var result = stockInTrade.getTradeList().stream()
                .filter(
                        o -> o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                                .isAfter(LocalDate.now(ZoneId.systemDefault()).minusDays(days)))
                .collect(Collectors.toList());
        result.sort(Comparator.comparing(Trade::getDate));
        return result;
    }

    public List<Transaction> getTransactionByAccountId(User user, Long accountId, Long days){
        Account account = user.getAccountList().stream()
                .filter(o -> o.getId() == accountId)
                .findFirst().orElse(null);
        if(account==null)
            return null;
        var result = transactionRepository.getAllTransactionByAccountId(accountId).orElse(new ArrayList<>())
                .stream()
                .filter(
                        o -> o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                                .isAfter(LocalDate.now(ZoneId.systemDefault()).minusDays(days)))
                .collect(Collectors.toList())
                ;
        result.sort(Comparator.comparing(Transaction::getDate));
        return result;
    }
}
