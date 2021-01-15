package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockEventRepository extends CrudRepository<StockEvent, Long> {
    @Query(value = "select * from t_stock_event where stock_id = ?1", nativeQuery = true)
    Optional<List<StockEvent>> findByStockId(Long stockId);
}
