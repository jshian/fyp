package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockInTrade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockInTradeRepository extends CrudRepository<StockInTrade, Long> {


    Optional<StockInTrade> findByAccountIdAndStockId(Long accountId, Long StockId);

    Optional<List<StockInTrade>> findByAccountId(Long accountId);

    @Query(value = "select stock from t_stock_in_trade where account = ?1", nativeQuery = true)
    Optional<List<Stock>> findStockByAccount(Account account);



}
