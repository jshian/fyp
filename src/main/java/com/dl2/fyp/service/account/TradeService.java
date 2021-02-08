package com.dl2.fyp.service.account;

import com.dl2.fyp.entity.*;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.exception.ServiceException;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import com.dl2.fyp.repository.account.TradeRepository;
import com.dl2.fyp.repository.account.TransactionRepository;
import com.dl2.fyp.repository.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@Transactional
public class TradeService {

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

    public void addTrade(User user, Trade trade, Long stockId ){
        // get the json input
        Account cashAccount = accountRepository.findAccountByUserIdAndType(user.getId(), AccountCategory.CASH).orElse(null);
        Account stockAccount = accountRepository.findAccountByUserIdAndType(user.getId(), AccountCategory.STOCK).orElse(null);
        Stock stock = stockRepository.findById(stockId).orElse(null);
        Boolean action = trade.getAction();
        BigDecimal price = trade.getPrice();
        Long numOfShare = trade.getNumOfShare();
        StockInTrade stockInTrade = stockInTradeRepository.findByAccountIdAndStockId(stockAccount.getId(),stock.getId()).orElse(null);

        // check input
        if (stockAccount==null||cashAccount==null) throw new ServiceException(HttpStatus.NOT_FOUND,"account not founded");
        else if (stock==null) throw new ServiceException(HttpStatus.NOT_FOUND,"stock not founded");
        else if (stockInTrade==null){
            if (action==true){
                stockInTrade = new StockInTrade();
                stockInTrade.setAccount(stockAccount);
                stockInTrade.setStock(stock);
                stockInTrade.setNumOfShare(0L);
                stockInTrade.setAverageCost(BigDecimal.ZERO);
            }else
            throw new ServiceException(HttpStatus.BAD_REQUEST, "have not hold the stock yet");
        }else if (action==true && cashAccount.getAmount().compareTo(price.multiply(new BigDecimal(numOfShare)))<1)
            throw new ServiceException(HttpStatus.BAD_REQUEST,"not enough cash in Cash Account");
        else if (action==false && stockInTrade.getNumOfShare()<numOfShare){
            throw new ServiceException(HttpStatus.BAD_REQUEST,"not enough numOfShare in StockInTrade");
        }

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
    }

    public void addTransaction(Account accountIn, Account accountOut, BigDecimal amount) {
        if (accountIn==null||accountOut==null) throw new ServiceException(HttpStatus.NOT_FOUND,"account not founded");
        else if (amount.compareTo(BigDecimal.ZERO)<1) throw new ServiceException(HttpStatus.NOT_FOUND, "require valid transaction amount");
        else if (accountOut.getAmount().compareTo(amount)<1 && accountOut.getCategory() != AccountCategory.STOCK) throw new ServiceException(HttpStatus.BAD_REQUEST, "not enough cash in account");
        Transaction transaction = new Transaction();

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

    // sql version not tested
    public List<Trade> getTradeByStockInTradeId2(Long stockInTradeId, Long days){
        return tradeRepository.findOrderedTradeListByIdAndDate(stockInTradeId,
                LocalDate.now(ZoneId.systemDefault()).minusDays(days)).orElse(null);
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

    //sql version not tested
    public List<Transaction> getTransactionByAccountId2(Long accountId, Long days){
        return transactionRepository.getOrderedTransactionByAccountIdAndDate(accountId,
                LocalDate.now(ZoneId.systemDefault()).minusDays(days)).orElse(null);
    }
}
