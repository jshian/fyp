package com.dl2.fyp;


import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void addUserInfoTest(){
        UserInfo userInfo = new UserInfo();
        User user = userService.addUserInfo(userInfo, 1L);
        System.out.println(user);
    }
}
