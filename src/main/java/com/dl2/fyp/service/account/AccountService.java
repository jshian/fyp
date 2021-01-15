package com.dl2.fyp.service.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.repository.account.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private static Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    /**
     * return all accounts under the given user id
     * @param userId
     * @return
     */
    public List<Account> getAllAccount(Long userId){
        if(userId == null) return null;
        LOG.debug("get accounts, id={}", userId);
        List<Account> accounts = accountRepository.findAllAccount(userId).orElse(null);
        if (accounts==null && accounts.size()<=0) return null;
        LOG.debug("get accounts, result={}", accounts);
        return accounts;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<StockInTrade> getAllStockInTrade(User user){
        Account account = null;
        for (Account _account:user.getAccountList()) {
            if(_account.getCategory() == AccountCategory.STOCK)
                account = _account;
        }
        if (account == null) return null;
        return account.getStockInTradesList().stream()
                .filter(o -> o.getNumOfShare() > 0)
                .collect(Collectors.toList());
    }

    public Boolean updateStockAccount(Account account){
        try{
            BigDecimal accountAmt = new BigDecimal(0);
            for (StockInTrade stockInTrade :account.getStockInTradesList()) {
                accountAmt.add(stockInTrade.getStock().getCurrentPrice().multiply(BigDecimal.valueOf(stockInTrade.getNumOfShare())));
            }
            account.setAmount(accountAmt);
            accountRepository.save(account);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
