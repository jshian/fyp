package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.trade.TradeInputDto;
import com.dl2.fyp.dto.transaction.TransactionDto;
import com.dl2.fyp.dto.transaction.TransactionInputDto;
import com.dl2.fyp.entity.*;
import com.dl2.fyp.service.account.AccountService;
import com.dl2.fyp.service.account.TradeService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/account")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/trade/Add")
    public Result addTrade(@RequestBody TradeInputDto tradeInputDto,@RequestBody User user){
        Trade trade = new Trade();
        BeanUtils.copyProperties(tradeInputDto, trade);
        tradeService.addTrade(user,trade, tradeInputDto.getStockId());
        return ResultUtil.success("added trading stock");
    }

    @PostMapping("/transaction/Add")
    public Result addTransaction(@RequestBody TransactionInputDto transactionInputDto, @RequestBody User user){
        Account accountIn =  accountService.getAccountById(transactionInputDto.getAccountInId());
        Account accountOut = accountService.getAccountById(transactionInputDto.getAccountOutId());
        tradeService.addTransaction(accountIn, accountOut, transactionInputDto.getAmount());
        return ResultUtil.success("added transaction");
    }

    @GetMapping("/trade/Get")
    public Result getTrade(@RequestParam Long stockInTradeId, @RequestParam Long days, @RequestBody User user){
        List<Trade> tradeList = tradeService.getTradeByStockInTradeId(user, stockInTradeId, days);
        return ResultUtil.success(tradeList);
    }

    @GetMapping("/transaction/Get")
    public Result getTransaction(@RequestParam Long accountId, @RequestParam Long days, @RequestBody User user){
        List<Transaction> transactionList = tradeService.getTransactionByAccountId(user, accountId, days);
        List<TransactionDto> dtoList = new ArrayList<>();
        for (Transaction transaction: transactionList) {
            dtoList.add(new TransactionDto(transaction, accountId));
        }
        return ResultUtil.success(dtoList);
    }
}
