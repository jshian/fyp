package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "t_stock")
@Table(indexes = @Index(name = "code_risk",columnList = "code, riskIndex", unique = true))
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SectorCategory sector;

    @OneToMany(mappedBy = "stock",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Valid
    @JsonIgnore
    @ToString.Exclude
    private List<StockInTrade> stockInTradeList  = new ArrayList<>();;

    @Column(name = "code", length = 6)
    @NotNull
    private String code;

    @Column(name = "name")
    @NotNull
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdated = new Date();

    @Min(value = 0, message = "invalid negative input")
    private BigDecimal currentPrice;
    private BigDecimal upperStop;
    private BigDecimal downStop;
    private BigDecimal accuracy;
    private Boolean isDelist = false;
    @Min(value = 0, message = "invalid negative input")
    private Integer holdingPeriod;
    @Temporal(TemporalType.DATE)
    private Date sellOutDate;
    private BigDecimal riskIndex;
}
