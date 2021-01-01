package com.dl2.fyp.entity;

import com.dl2.fyp.enums.SectorCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name="t_market_event")
public class MarketEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection(targetClass=SectorCategory.class)
    private List<SectorCategory> sector;
    @Range(min=0,max=1,message = "Out of range")
    @NotNull
    private BigDecimal severity;
    @NotNull
    private Integer expectedPeriod;
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
