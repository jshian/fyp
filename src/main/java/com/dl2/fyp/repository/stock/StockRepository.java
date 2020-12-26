package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {

    Optional<Stock> findByCode(String code);
}
