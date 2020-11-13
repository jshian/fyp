package com.dl2.fyp.service;

import com.dl2.fyp.entity.User;

public interface AuthService {
    User register(User userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
