package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Long> {

    @Query(value = "select * from t_trade where stock_in_trade_id = ?1", nativeQuery = true)
    public Optional<List<Trade>> getAllTradeByStockInTradeId(Long stockInTradeId);
}
