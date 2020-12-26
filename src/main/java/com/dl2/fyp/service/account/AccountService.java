package com.dl2.fyp.service.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.repository.account.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        List list = new LinkedList();
        for (Account account : accounts) {
            Map map = new HashMap<String, Object>();
            map.put("id",account.getId());
            map.put("category", account.getCategory());
            map.put("amount", account.getAmount());
            list.add(map);
        }
        LOG.debug("get accounts, result={}", accounts);
        return accounts;
    }

    /**
     *
     * @param id
     * @return
     */
    public List getAllStockInTrade(Long id){
        LOG.debug("find all stock in trade in specific account, id={}", id);
        List<Map> mapList = new LinkedList<>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) return null;
        for (StockInTrade inTrade : account.getStockInTradesList()) {
            Stock stock = inTrade.getStock();
            Map map = new HashMap<String, Object>();
            map.put("stockInTradeId",inTrade.getId());
            map.put("symbol",stock.getCode());
            map.put("noOfShare",inTrade.getNumOfShare());
            map.put("currentPrice",stock.getCurrentPrice());
            map.put("cost", inTrade.getAverageCost());
            mapList.add(map);
        }
        LOG.debug("find all stock in trade in specific account, result={}", mapList);
        return mapList;
    }
}
