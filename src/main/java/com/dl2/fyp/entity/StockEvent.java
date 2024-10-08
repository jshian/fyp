package com.dl2.fyp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "t_stock_event")
@Table(indexes = @Index(name = "stock_time", columnList = "stock_id,datetime"))
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id")
    @JsonIgnore
    private Stock stock;
    @Range(min=-1,max=1,message = "Out of range")
    private BigDecimal severity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "link")
    @NotNull
    private String link;
}
