package com.dl2.fyp.controller;


import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.service.account.AccountService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/{userId}")
    public Result getAllAccount(@PathVariable Long userId){
        if(userId==null) return ResultUtil.error(-1,"invalid input");
        List<Account> accounts = accountService.getAllAccount(userId);
        if(accounts == null) return ResultUtil.error(-1,"account not founded");
        return ResultUtil.success(accounts);
    }

    @PostMapping(value = "/stockInTrade/{id}")
    public Result getAllStockInTrade(@PathVariable Long id){
        if(id == null) return ResultUtil.error(-1, "invalid input");
        List list = accountService.getAllStockInTrade(id);
        if (list == null) return ResultUtil.error(-1, "failed to get");
        return ResultUtil.success(list);
    }
}
