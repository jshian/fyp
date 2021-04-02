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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;


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

    @PostMapping("/info/add")
    public Result addUserInfo(@RequestBody UserInfo userInfo, Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        long id = user.getId();
        if(userInfo == null || user == null)
            return ResultUtil.error(400, "invalid input");
        if(userService.addUserInfo(userInfo, id) == null){
            return ResultUtil.error(401,"error");
        }
        Account cashAccount = new Account();
        cashAccount.setCategory(AccountCategory.CASH);
        cashAccount.setAmount(userInfo.getCashFlow());

        Account stockAccount = new Account();
        stockAccount.setCategory(AccountCategory.STOCK);

        if (userService.addAccount(cashAccount, id)==null) {
            return ResultUtil.error(1, "added failed");
        }
        if (userService.addAccount(stockAccount, id)==null) {
            return ResultUtil.error(1, "added failed");
        }
        return ResultUtil.success("added user info");
    }

    @GetMapping("/info/get")
    public ResponseEntity<Result> getUserInfo(Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if (user == null)
        {
            return new ResponseEntity<Result>(
                    ResultUtil.error(404,"can't find user")
                    , HttpStatus.valueOf(404)
            );
        }
        UserInfo userInfo = user.getUserInfo();
        if(userInfo == null)
        {
            return new ResponseEntity<Result>(
                    ResultUtil.error(403,"First Login")
                    , HttpStatus.valueOf(403)
            );
        }
        return new ResponseEntity<Result>(
                ResultUtil.success(user.getUserInfo())
                , HttpStatus.valueOf(200)
        );
    }

    @PutMapping("/info/update")
    public Result updateUserInfo(@RequestBody UserInfo userInfo,Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(user==null)
            return ResultUtil.error(-1,"invalid id");
        UserInfo oldInfo = user.getUserInfo();
        if(oldInfo==null)
            return ResultUtil.error(-1, "user info have not been set");
        UserInfo newInfo = userInfoService.updateUserInfo(oldInfo,userInfo);
        user.setUserInfo(newInfo);
        if(userService.addUser(user) == null)
            return ResultUtil.error(-1, "update failed");
        return ResultUtil.success(user);
    }

    @PostMapping("/device/add")
    public Result addUserDevice(@RequestBody UserDevice userDevice, Principal principal){
        User user = userService.findByFirebaseUid(principal.getName());
        if(userDevice == null || user == null) return ResultUtil.error(-1, "invalid input");
        if(userService.addUserDevice(userDevice, user) == null){
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
