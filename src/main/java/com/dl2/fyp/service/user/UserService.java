package com.dl2.fyp.service.user;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.user.UserDeviceRepository;
import com.dl2.fyp.repository.user.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;


@Service
public class UserService{
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * return user after adding user entity, return null if not success
     * @param user
     * @return
     */
    @Transactional
    public User addUser(User user){
        LOG.debug("add user, param ={}",user);
        User u;
        try{
            u = userRepository.save(user);
        }catch (IllegalArgumentException e){
            return null;
        }
        LOG.debug("add user, result={}",u);
        return u;
    }


    public Long countAll() {
        return userRepository.countAll();
    }

    public User findByFirebaseUid(String firebaseUid){
        if(firebaseUid == null) return null;
        LOG.debug("find one user, param={}", firebaseUid);
        User user = userRepository.findByFirebaseUid(firebaseUid);
        LOG.debug("find one user, result={}", user);
        return user;
    }

    /**
     * find one user
     * @param id
     * @return
     */
    public User find(Long id){
        if(id == null) return null;
        LOG.debug("find one user, param={}", id);
        User user = userRepository.findById(id).orElse(null);
        LOG.debug("find one user, result={}", user);
        return user;
    }

    //for test
    @Transactional
    public User addUser(Long id){
        User user = new User();
        user.setFirebaseUid("test");
        user.setId(id);
        return addUser(user);
    }

    /**
     * add user info and return the user
     * @param userInfo
     * @param id
     * @return
     */
    @Transactional
    public User addUserInfo(UserInfo userInfo, Long id){
        LOG.debug("add user info, param={}",userInfo);
        User user = userRepository.findById(id).orElse(null);
        // can't find user record or user info already exists
        if (user == null || user.getUserInfo()!=null)
            return null;
        UserInfo info = userInfoService.setUserInfo(userInfo);
        if (info == null)
            return null;
        user.setUserInfo(info);
        try{
            userRepository.save(user);
        }catch (Exception e){
            return null;
        }
        LOG.debug("add user info, result={}",userInfo);
        return user;
    }

    @Transactional
    public UserDevice addUserDevice(UserDevice userDevice, User user){
        LOG.debug("add user device, param={}",userDevice);
        if(user == null) return null;
        try {
            user.getUserDevice().add(userDevice);
            userRepository.save(user);
        }catch (IllegalArgumentException e){
            return null;
        }
        LOG.debug("add user device, result={}",userDevice);
        return userDevice;
    }

    /**
     * add account
     * @param account
     * @param userId
     * @return
     */
    @Transactional
    public Account addAccount(Account account, Long userId){
        LOG.debug("add account, param:{}", account);
        User user = userRepository.findById(userId).orElse(null);
        if(user == null || account.getCategory()==null) return null;
        try {
            List<Account> list = user.getAccountList();
            if (list == null) return null;
            for (Account a : list) {
                if (a.getCategory().getCategory().toLowerCase().equals(account.getCategory().getCategory().toLowerCase())){
                    return null;
                }
            }
            account.setUser(user);
            list.add(account);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            return null;
        }
        LOG.debug("add account, result:{}", account);
        return account;
    }
}
