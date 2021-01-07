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

    private Date datetime;
    @Column(name = "title", columnDefinition = "text")
    @NotNull
    private String title;
    @Column(name = "content", columnDefinition = "text")
    @NotNull
    private String content;
    @Column(name = "link")
    @NotNull
    private String link;
}
