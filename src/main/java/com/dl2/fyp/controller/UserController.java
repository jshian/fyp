package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
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

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;


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
        return userService.addUserInfo(userInfo, id);
    }

    @PostMapping("/account/add/{id}")
    public Result addAccount(@RequestBody Account account, @PathVariable Long id){
        return userService.addAccount(account, id);
    }

    @GetMapping("/info/get/{id}")
    public Result getUserInfo(@PathVariable Long id){
        User user = userService.find(id);
        Assert.notNull(user,"id not exist");
        return ResultUtil.success(user.getUserInfo());
    }

    @PostMapping("/info/update/{id}")
    public Result updateUserInfo(@RequestBody UserInfo userInfo,@PathVariable Long id){
        User user = userService.find(id);
        Assert.notNull(user,"id not exist");
        UserInfo oldInfo = user.getUserInfo();
        Assert.notNull(user,"info does not created");
//        userInfo.setId(oldInfo.getId());
        UserInfo newInfo = userInfoService.updateUserInfo(oldInfo,userInfo);
        user.setUserInfo(newInfo);
        userService.addUser(user);
//        userInfoService.updateUserInfo(userInfo);
        return ResultUtil.success(user);
    }

    @GetMapping("/account/{id}")
    public Result getAllAccount(@PathVariable Long id){
        List accounts = userService.getAllAccount(id);
        return ResultUtil.success(accounts);
    }

    @GetMapping("/account/stockInTrade/{id}")
    public String getAllStockInTrade(@PathVariable Long id, HttpServletRequest httpServletRequest){
        List<Account> accounts = userService.getAllAccount(id);
        List<Long> ids = new LinkedList<>();
        for (int i = 0; i < accounts.size(); i++) {
            ids.add(accounts.get(i).getId());
        }
        httpServletRequest.setAttribute("ids", ids);
        return "forward:/account/stockInTrade/";
    }

    // for test
    @PostMapping("/add/{id}")
    public Result addUser(@PathVariable Long id){
        return userService.addUser(id);
    }

}
