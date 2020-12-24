package com.dl2.fyp.repository;

import com.dl2.fyp.entity.MarketEvent;
import org.springframework.data.repository.CrudRepository;

public interface MarketRepository extends CrudRepository<MarketEvent, Long> {
}
