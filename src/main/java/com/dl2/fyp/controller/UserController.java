package com.dl2.fyp.controller;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.enums.AccountCategory;
import com.dl2.fyp.service.user.UserInfoService;
import com.dl2.fyp.service.user.UserService;
import com.dl2.fyp.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

//    @PostMapping("/count")
//    public Result countUser(){
//        return ResultUtil.success(userService.countAll());
//    }

    @PostMapping("/info/add")
    public Result addUserInfo(@RequestBody UserInfo userInfo, @RequestBody User user){
        long id = user.getId();
        if(userService.addUserInfo(userInfo, id) == null){
            return ResultUtil.error(HttpStatus.FORBIDDEN,"Added user information failed");
        }
        Account cashAccount = new Account();
        cashAccount.setCategory(AccountCategory.CASH);
        cashAccount.setAmount(userInfo.getCashFlow());

        Account stockAccount = new Account();
        stockAccount.setCategory(AccountCategory.STOCK);

        if (userService.addAccount(cashAccount, id)==null) {
            return ResultUtil.error(HttpStatus.FORBIDDEN, "Added cash account failed");
        }
        if (userService.addAccount(stockAccount, id)==null) {
            return ResultUtil.error(HttpStatus.FORBIDDEN, "Added stock account failed");
        }
        return ResultUtil.success("Added user info");
    }

    @GetMapping("/info/get")
    public Result getUserInfo(@RequestBody User user){
        UserInfo userInfo = user.getUserInfo();
        if(userInfo == null)
            return ResultUtil.error(HttpStatus.FORBIDDEN,"First Login");
        return ResultUtil.success(user.getUserInfo());
    }

    @PutMapping("/info/update")
    public Result updateUserInfo(@RequestBody UserInfo userInfo,@RequestBody User user){
        UserInfo oldInfo = user.getUserInfo();
        if(oldInfo==null)
            return ResultUtil.error(HttpStatus.NOT_FOUND, "User info have not been set");
        UserInfo newInfo = userInfoService.updateUserInfo(oldInfo,userInfo);
        user.setUserInfo(newInfo);
        if(userService.addUser(user) == null)
            return ResultUtil.error(HttpStatus.FORBIDDEN, "Update failed");
        return ResultUtil.success(user);
    }

    @PostMapping("/device/add/{id}")
    public Result addUserDevice(@RequestBody UserDevice userDevice,@RequestBody User user){
        if(userService.addUserDevice(userDevice, user.getId()) == null){
            return ResultUtil.error(HttpStatus.FORBIDDEN,"Added user device failed");
        }
        return ResultUtil.success("Added user info");
    }

    @PostMapping("/account/add/{id}")
    public Result addAccount(@RequestBody Account account,@RequestBody User user){
        if (userService.addAccount(account, user.getId())==null) {
            return ResultUtil.error(HttpStatus.FORBIDDEN, "Added account failed");
        }
        return ResultUtil.success("Added account");
    }

    // for test
//    @PostMapping("/add/{id}")
//    public Result addUser(@PathVariable Long id){
//        User u = userService.addUser(id);
//        if(u == null) ResultUtil.error(HttpStatus.FORBIDDEN, "Add user failed");
//        return ResultUtil.success("Added user");
//    }

}
