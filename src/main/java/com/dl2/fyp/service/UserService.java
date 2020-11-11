package com.dl2.fyp.service;

import com.dl2.fyp.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    public Integer countAll() {
        return userRepository.countAll();
    }

}
