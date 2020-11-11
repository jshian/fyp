package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity(name="t_market_event")
public class MarketEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private List<SectorCategory> sector = new LinkedList<>();
    @Range(min=0,max=1,message = "Out of range")
    private Float severity;
    private Integer expectedPeriod;
    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "source", columnDefinition = "text")
    private String source;
}
