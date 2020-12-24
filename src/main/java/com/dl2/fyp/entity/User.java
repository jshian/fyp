package com.dl2.fyp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity(name = "t_user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @NotNull
    private String firebaseUid;

    @Email
    private String email;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Account> accountList;
}
