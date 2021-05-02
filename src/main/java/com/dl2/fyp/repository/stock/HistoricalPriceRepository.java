package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.HistoricalPrice;
import com.dl2.fyp.entity.PredictedPrice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalPriceRepository extends CrudRepository<HistoricalPrice, Long> {
    @Query(value = "select * from (select  * from t_historical_price where code = ?1 ORDER BY date desc LIMIT 250 ) as stock ORDER BY date",
            nativeQuery = true)
    List<HistoricalPrice> getHistoricalPriceByCode(String code);

}
