package com.dl2.fyp.repository.stock;

import com.dl2.fyp.entity.PredictedPrice;
import com.dl2.fyp.entity.StockEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictedPriceRepository  extends CrudRepository<PredictedPrice, Long> {
    @Query(value = "select * from t_predict_price where code = ?1 order by day",
            nativeQuery = true)
    List<PredictedPrice> getPredictedPriceByCode(String code);

}
