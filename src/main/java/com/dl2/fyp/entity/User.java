package com.dl2.fyp.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "t_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @Length(min = 3, max=20)
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Getter
    @Setter
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.DETACH})
    private Account account;
}
