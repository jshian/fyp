package com.dl2.fyp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "t_user_device")
@Table(indexes = @Index(name = "firebaseDeviceId", columnList = "firebaseDeviceId", unique = true))
public class UserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE},optional = false)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;

    @NotNull
    private String firebaseDeviceId;
}
