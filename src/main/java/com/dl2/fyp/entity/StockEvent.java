package com.dl2.fyp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.sql.Date;

@Data
@Getter
@Setter
@Entity(name = "t_stock_event")
public class StockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Range(min=0,max=1,message = "Out of range")
    private Float severity;
    private Integer expectedPeriod;
    @Temporal(TemporalType.TIMESTAMP)
    private Date begin;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedEnd;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "source", columnDefinition = "text")
    private String source;
}
