package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.StockInTrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInTradeRepository extends CrudRepository<StockInTrade, Long> {
}
