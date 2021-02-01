package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.dto.trade.TradeInputDto;
import com.dl2.fyp.dto.transaction.TransactionDto;
import com.dl2.fyp.dto.transaction.TransactionInputDto;
import com.dl2.fyp.entity.*;
import com.dl2.fyp.service.account.TradeService;
import com.dl2.fyp.service.user.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/account")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    @PostMapping("/trade/Add")
    public Result addTrade(@RequestBody TradeInputDto tradeInputDto, Principal principal){
        if(tradeInputDto==null) return ResultUtil.error(-1, "invalid input");
        Trade trade = new Trade();
        BeanUtils.copyProperties(tradeInputDto, trade);
        return tradeService.addTrade(userService.findByFirebaseUid(principal.getName()),trade, tradeInputDto.getStockId());
    }

    @PostMapping("/transaction/Add")
    public Result addTransaction(@RequestBody TransactionInputDto transactionInputDto, Principal principal){
        if(transactionInputDto==null) return ResultUtil.error(-1, "invalid input");
        User user = userService.findByFirebaseUid(principal.getName());
        Account accountIn = user.getAccountList().stream()
                .filter(o -> o.getId() == transactionInputDto.getAccountInId())
                .findAny().orElse(null);
        Account accountOut = user.getAccountList().stream()
                .filter(o -> o.getId() == transactionInputDto.getAccountOutId())
                .findAny().orElse(null);
        return tradeService.addTransaction(accountIn, accountOut, transactionInputDto.getAmount());
    }

    @GetMapping("/trade/Get")
    public Result getTrade(@RequestParam Long stockInTradeId, @RequestParam Long days, Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        List<Trade> tradeList = tradeService.getTradeByStockInTradeId(user, stockInTradeId, days);
        if(tradeList==null)
            return ResultUtil.error(-1, "invalid input");
        return ResultUtil.success(tradeList);
    }

    @GetMapping("/transaction/Get")
    public Result getTransaction(@RequestParam Long accountId, @RequestParam Long days, Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        List<Transaction> transactionList = tradeService.getTransactionByAccountId(user, accountId, days);
        if(transactionList==null)
            return ResultUtil.error(-1, "invalid input");
        List<TransactionDto> dtoList = new ArrayList<>();
        for (Transaction transaction: transactionList) {
            dtoList.add(new TransactionDto(transaction, accountId));
        }
        return ResultUtil.success(dtoList);
    }
}
