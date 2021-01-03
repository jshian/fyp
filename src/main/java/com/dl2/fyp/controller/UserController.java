package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.service.user.UserInfoService;
import com.dl2.fyp.service.user.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(userInfo == null || id == null) return ResultUtil.error(-1, "invalid input");
        if(userService.addUserInfo(userInfo, id) == null){
            return ResultUtil.error(-1,"error");
        }
        return ResultUtil.success("added user info");
    }

    @GetMapping("/info/get/{id}")
    public Result getUserInfo(@PathVariable Long id){
        User user = userService.find(id);
        if (user == null) return ResultUtil.error(-1,"can't find user");
        return ResultUtil.success(user.getUserInfo());
    }

    @PostMapping("/info/update/{id}")
    public Result updateUserInfo(@RequestBody UserInfo userInfo,@PathVariable Long id){
        User user = userService.find(id);
        if(user==null) return ResultUtil.error(-1,"invalid id");
        UserInfo oldInfo = user.getUserInfo();
        if(oldInfo==null) return ResultUtil.error(-1, "user info have not been set");
        UserInfo newInfo = userInfoService.updateUserInfo(oldInfo,userInfo);
        user.setUserInfo(newInfo);
        if(userService.addUser(user) == null) return ResultUtil.error(-1, "update failed");
        return ResultUtil.success(user);
    }

    @PostMapping("/device/add/{id}")
    public Result addUserDevice(@RequestBody UserDevice userDevice, @PathVariable Long id){
        if(userDevice == null || id == null) return ResultUtil.error(-1, "invalid input");
        if(userService.addUserDevice(userDevice, id) == null){
            return ResultUtil.error(-1,"error");
        }
        return ResultUtil.success("added user info");
    }

    @PostMapping("/account/add/{id}")
    public Result addAccount(@RequestBody Account account, @PathVariable Long id){
        if(account == null || id == null) return ResultUtil.error(-1, "invalid input");
        if (userService.addAccount(account, id)==null) {
            return ResultUtil.error(1, "added failed");
        }
        return ResultUtil.success("added account");
    }

    // for test
    @PostMapping("/add/{id}")
    public Result addUser(@PathVariable Long id){
        User u = userService.addUser(id);
        if(u == null) ResultUtil.error(-1, "add failed");
        return ResultUtil.success("added user");
    }

}
