package com.dl2.fyp.service.user;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class UserService{

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
    public User addUser(User user) throws IllegalArgumentException{
        return userRepository.save(user);
    }


    public Long countAll() {
        return userRepository.countAll();
    }

    public User findByFirebaseUid(String firebaseUid){
        User user = userRepository.findByFirebaseUid(firebaseUid);
        return user;
    }

    /**
     * find one user
     * @param id
     * @return
     */
    public User find(Long id){
        User user = userRepository.findById(id).orElse(null);
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
    public User addUserInfo(UserInfo userInfo, Long id) throws IllegalArgumentException{
        User user = userRepository.findById(id).orElse(null);
        // can't find user record or user info already exists
        if (user == null || user.getUserInfo()!=null)
            return null;
        UserInfo info = userInfoService.setUserInfo(userInfo);
        if (info == null)
            return null;
        user.setUserInfo(info);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserDevice addUserDevice(UserDevice userDevice, Long id) throws IllegalArgumentException{
        User user = userRepository.findById(id).orElse(null);
        if(user == null) return null;
        user.getUserDevice().add(userDevice);
        userRepository.save(user);
        return userDevice;
    }

    /**
     * add account
     * @param account
     * @param userId
     * @return
     */
    @Transactional
    public Account addAccount(Account account, Long userId) throws IllegalArgumentException{
        User user = userRepository.findById(userId).orElse(null);
        if(user == null || account.getCategory()==null) return null;
        List<Account> list = user.getAccountList();
        if (list == null) return null;
        for (Account a : list) {
            if (a.getCategory().getCategory().toLowerCase().equals(account.getCategory().getCategory().toLowerCase())) {
                return null;
            }
        }
        list.add(account);
        userRepository.save(user);
        return account;
    }
}
