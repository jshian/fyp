package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserPorfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private StockInfo stockInfo;
    private Integer recommended;
    private Integer holding;
    private Integer buyPrice;
}
