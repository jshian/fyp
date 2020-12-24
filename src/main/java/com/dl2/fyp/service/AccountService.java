package com.dl2.fyp.service;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {
    private static Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    /**
     *
     * @param account
     * @return
     */
    public Account setAccount(Account account){
        LOG.debug("create user info, parameter:{}", account);

        checkInfo(account);

        setDefault(account);

//        UserInfo result = userInfoRepository.save(userInfo);
        LOG.debug("create user info, result:{}", account);
        return account;
    }

    /**
     *
     * @param account
     */
    private void setDefault(Account account) {

    }

    /**
     *
     * @param account
     */
    private void checkInfo(Account account) {

    }

//    public Result<Account>

    /**
     *
     * @param id
     * @return
     */
    public Map<String, Object> getAllStockInTrade(Long id){
        Assert.notNull(id, "required account id");
        LOG.debug("find all stock in trade in specific account, id={}", id);
        Map map = new HashMap<String, Object>();
        Account account = accountRepository.findById(id).orElse(null);
        for (StockInTrade inTrade : account.getStockInTradesList()) {
            Stock stock = inTrade.getStock();
            map.put("stockInTradeId",inTrade.getId());
            map.put("symbol",stock.getCode());
            map.put("noOfShare",inTrade.getNumOfShare());
            map.put("currentPrice",stock.getCurrentPrice());
            map.put("cost", inTrade.getAverageCost());
        }
        LOG.debug("find all stock in trade in specific account, result={}", map);
        return map;
    }
}
