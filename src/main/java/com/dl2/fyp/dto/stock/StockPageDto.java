package com.dl2.fyp.dto.stock;

import com.dl2.fyp.dto.stock_event.StockEventDto;
import com.dl2.fyp.entity.HistoricalPrice;
import com.dl2.fyp.entity.PredictedPrice;
import com.dl2.fyp.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockPageDto {
    private Stock stock;
    private List<StockEventDto> eventList;
    private List<String> labels;
    private List<PriceDto> historicalPriceList;
    private List<PriceDto> predictedPriceList;
}
