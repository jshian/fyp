package com.dl2.fyp.service.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.repository.account.AccountRepository;
import com.dl2.fyp.repository.account.StockInTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockInTradeRepository stockInTradeRepository;

    /**
     * return all accounts under the given user id
     * @param userId
     * @return
     */
    public List<Account> getAllAccount(Long userId){
        List<Account> accounts = accountRepository.findAllAccount(userId).orElse(null);
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

    public List<StockInTrade> getAllStockInTrade2(User user){
        Account account = null;
        for (Account _account:user.getAccountList()) {
            if(_account.getCategory() == AccountCategory.STOCK)
                account = _account;
        }
        if (account == null) return null;
        return stockInTradeRepository.findByAccountId(account.getId()).orElse(null);
    }

    public Boolean updateStockAccount(Account account) throws IllegalArgumentException{
        BigDecimal accountAmt = new BigDecimal(0);
        for (StockInTrade stockInTrade :account.getStockInTradesList()) {
            accountAmt.add(stockInTrade.getStock().getCurrentPrice().multiply(BigDecimal.valueOf(stockInTrade.getNumOfShare())));
        }
        account.setAmount(accountAmt);
        accountRepository.save(account);
        return true;
    }
}
