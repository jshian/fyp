package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity(name = "t_user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @Length(min = 3, max=20)
    private String name;

    @Size(min=1,max=10)
    private Byte[] password;

    @NotNull
    private String salt;

    @Email
    private String email;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.DETACH})
    private Account account;
}
