package com.dl2.fyp.controller;


import com.dl2.fyp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {
    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/stockInTrade/")
    public Map getAllStockInTrade(@RequestParam List<Long> ids){
        Map map = new HashMap<String, Object>();
        for (Long id : ids) {
            map.putAll(accountService.getAllStockInTrade(id));
        }
        return map;
    }
}
