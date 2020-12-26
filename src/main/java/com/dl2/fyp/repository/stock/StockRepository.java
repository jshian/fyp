package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {
}
