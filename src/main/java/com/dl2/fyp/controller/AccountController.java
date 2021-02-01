package com.dl2.fyp.controller;


import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.account.AccountForStatementDto;
import com.dl2.fyp.dto.stock_in_trade.StockInTradeDto;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockInTrade;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.service.account.AccountService;
import com.dl2.fyp.service.user.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @GetMapping("/GetCharts")
    public Result getAllAccountForCharts(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(user==null) return ResultUtil.error(-1,"invalid input");
        List<Account> accounts = user.getAccountList();
        if(accounts == null) return ResultUtil.error(-1,"account not founded");
        return ResultUtil.success(accounts);
    }

    @GetMapping("/GetAllForStatement")
    public Result getAllAccountForStatement(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(user==null)
            return ResultUtil.error(-1,"invalid input");
        List<Account> accounts = user.getAccountList();
        if(accounts == null)
            return ResultUtil.error(-1,"account not founded");
        List<AccountForStatementDto> dtoList = new ArrayList<>();
        for (Account account : accounts){
            dtoList.add(new AccountForStatementDto(account));
        }
        return ResultUtil.success(dtoList);
    }

    @GetMapping(value = "/stockInTrade")
    public Result getAllStockInTrade(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(user == null) return ResultUtil.error(-1, "invalid input");
        List<StockInTrade> list = accountService.getAllStockInTrade(user);
        List<Map> mappedList = new LinkedList<>();
        for (StockInTrade stockInTrade : list) {
            if(stockInTrade.getNumOfShare() > 0){
                Stock stock = stockInTrade.getStock();
                Map map = new HashMap<String, Object>();
                map.put("stockInTradeId",stockInTrade.getId());
                map.put("symbol",stock.getCode());
                map.put("noOfShare",stockInTrade.getNumOfShare());
                map.put("currentPrice",stock.getCurrentPrice());
                map.put("cost", stockInTrade.getAverageCost());
                mappedList.add(map);
            }
        }
        if (mappedList == null) return ResultUtil.error(-1, "failed to get");
        return ResultUtil.success(mappedList);
    }

    @GetMapping(value = "/stockInTrade/detail")
    public Result getAllStockInTradeDetail(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(user == null) return ResultUtil.error(-1, "invalid input");
        List<StockInTrade> list = accountService.getAllStockInTrade(user);
        if (list == null) return ResultUtil.error(-1, "failed to get");
        List<StockInTradeDto> dtoList = new ArrayList<>();
        for (StockInTrade stockInTrade: list) {
            dtoList.add(new StockInTradeDto(stockInTrade));
        }
        return ResultUtil.success(dtoList);
    }
}
