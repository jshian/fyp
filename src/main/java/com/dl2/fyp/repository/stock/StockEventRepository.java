package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.StockEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockEventRepository extends CrudRepository<StockEvent, Long> {

    Optional<StockEvent> findByStock(Stock stock);
}
