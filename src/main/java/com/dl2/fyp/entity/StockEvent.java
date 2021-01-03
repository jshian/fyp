package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_stock_event")
public class StockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id")
    private Stock stock;
    @Range(min=0,max=1,message = "Out of range")
    @NotNull
    private BigDecimal severity;
    @NotNull
    private Integer expectedPeriod;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date begin;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date expectedEnd;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @LastModifiedDate
    private Date lastUpdate;
    @Column(name = "title", columnDefinition = "text")
    @NotNull
    private String title;
    @Column(name = "description", columnDefinition = "text")
    @NotNull
    private String description;
    @Column(name = "source", columnDefinition = "text")
    @NotNull
    private String source;
}
