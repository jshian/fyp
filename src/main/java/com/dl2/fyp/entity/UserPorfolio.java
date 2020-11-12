package com.dl2.fyp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity
public class UserPorfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Stock stock;
    private Integer recommended;
    private Integer holding;
    private Integer buyPrice;
}
