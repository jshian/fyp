package com.dl2.fyp.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Date;
import java.util.List;

@Data
@Entity(name = "t_user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Length(min = 3, max=20)
    private String name;

    @Column(name="password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @Email
    private String email;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.DETACH})
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    private List<String> roles;
}
