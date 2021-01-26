package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock, Long> {

    @Query(value = "select * from t_stock where code = ?1", nativeQuery = true)
    Stock findByCode(String code);

    @Query(value = "select * from t_stock where code like %?1% order by ?#{#pageable}",
            countQuery = "select count(*) from t_stock where code like %?1%",
            nativeQuery = true)
    List<Stock> findStocksByKeyword(String keyword, Pageable pageable);

    Page<Stock> findByCodeContaining(String keyword, Pageable pageable);
}
