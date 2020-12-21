package com.dl2.fyp.domain;

import com.dl2.fyp.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory(){}

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getFirebaseUid(),
                user.getEmail()
        );
    }
}
