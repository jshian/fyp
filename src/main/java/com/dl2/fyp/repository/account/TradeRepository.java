package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Long> {

    @Query(value = "select * from t_trade where stock_in_trade_id = ?1", nativeQuery = true)
    Optional<List<Trade>> getAllTradeByStockInTradeId(Long stockInTradeId);


    @Query(value = "select * from t_trade where stock_in_trade_id = ?1 " +
            "and date > ?2 order by date desc", nativeQuery = true)
    Optional<List<Trade>> findOrderedTradeListByIdAndDate(Long stockInTradeId, LocalDate localDate);
}
