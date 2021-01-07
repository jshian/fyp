package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import com.dl2.fyp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {

    @Query(value = "select * from t_stock where code = ?1", nativeQuery = true)
    Stock findByCode(String code);
}
