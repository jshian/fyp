package com.dl2.fyp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity(name = "t_user")
@Table(indexes = @Index(name = "firebaseUid", columnList = "firebaseUid", unique = true))
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id", referencedColumnName = "id")
    @Valid
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Valid
    @JsonIgnore
    private List<Account> accountList;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @Valid
    private List<UserDevice> userDevice;

    @NotNull
    private String firebaseUid;

    @Email
    private String email;
}
