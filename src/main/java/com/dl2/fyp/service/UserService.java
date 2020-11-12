package com.dl2.fyp.service;

import com.dl2.fyp.dto.user.*;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    public Integer countAll() {
        return userRepository.countAll();
    }

    public String login(UserLoginDto userLoginDto){
        return null;
    }

    public String register(UserRegisterDto userRegisterDto){
        User user = new User();
        user.setUsername(userRegisterDto.username);
        user.setPassword(bCryptPasswordEncoder().encode(userRegisterDto.password));
        user.setEmail(userRegisterDto.email);
        return login(new UserLoginDto(userRegisterDto.username, userRegisterDto.password));
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
