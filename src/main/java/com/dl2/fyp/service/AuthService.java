package com.dl2.fyp.service;

import com.dl2.fyp.entity.User;

import java.io.IOException;

public interface AuthService {
    User register(User userToAdd);
    String login(String token) throws IOException;
    String refresh(String oldToken);
}
