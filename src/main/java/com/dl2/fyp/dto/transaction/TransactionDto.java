package com.dl2.fyp.dto.transaction;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Transaction;
import com.dl2.fyp.enums.AccountCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TransactionDto {
    private String date = null;
    private BigDecimal balance;
    private AccountCategory accountTo = null;
    private AccountCategory accountFrom = null;
    private BigDecimal withdraw = null;
    private BigDecimal deposit = null;

    public TransactionDto(Transaction transaction, Long accountId){
        this.setDate(new SimpleDateFormat("yyyy-MM-dd").format(transaction.getDate()));
        if(transaction.getAccountIn().getId() == accountId){
            this.setBalance(transaction.getAccountInAmountAfter());
            this.setAccountFrom(transaction.getAccountOut().getCategory());
            this.setDeposit(transaction.getAmount());
        }else{
            this.setBalance(transaction.getAccountOutAmountAfter());
            this.setAccountTo(transaction.getAccountIn().getCategory());
            this.setWithdraw(transaction.getAmount());
        }
    }
}
