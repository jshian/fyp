package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.service.UserInfoService;
import com.dl2.fyp.service.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/count")
    public Result countUser(){
        return ResultUtil.success(userService.countAll());
    }

    @PostMapping("/info/add/{id}")
    public Result addUserInfo(@RequestBody UserInfo userInfo, @PathVariable Long id){
        User user = userService.find(id);
        Assert.notNull(user,"id not exist");
        UserInfo result = userInfoService.addUserInfo(userInfo);
        user.setUserInfo(result);
        return ResultUtil.success(result);
    }

    @GetMapping("/info/get/{id}")
    public Result getUserInfo(@PathVariable Long id){
        User user = userService.find(id);
        Assert.notNull(user,"id not exist");
        return ResultUtil.success(user);
    }

    @PostMapping("/info/update/{id}")
    public Result updateUserInfo(@RequestBody UserInfo userInfo,@PathVariable Long id){
        User user = userService.find(id);
        Assert.notNull(user,"id not exist");
        user.setUserInfo(userInfo);
        userInfoService.updateUserInfo(userInfo);
        return ResultUtil.success(user);
    }
}
