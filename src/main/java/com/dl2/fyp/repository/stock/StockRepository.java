package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock, Long> {

    @Query(value = "select * from t_stock where code = ?1", nativeQuery = true)
    Stock findByCode(String code);

    @Query(value = "select code from t_stock where isDelist = 0 order by code", nativeQuery = true)
    List<String> getAllCode();

    @Query(value = "select * from t_stock where code like %?1% order by ?#{#pageable}",
            countQuery = "select count(*) from t_stock where code like %?1%",
            nativeQuery = true)
    List<Stock> findStocksByKeyword(String keyword, Pageable pageable);

    @Modifying
    @Query(value = "update t_stock set sell_out_date = NULL where id > 0", nativeQuery = true)
    void clearSellOutDate();


    Page<Stock> findByCodeContaining(String keyword, Pageable pageable);
}
