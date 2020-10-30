package com.dl2.fyp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "t_user_device")
public class UserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;
}
