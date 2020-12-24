package com.dl2.fyp.service;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.UserRepository;
import com.dl2.fyp.util.ResultUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

    @Autowired
    private AccountService accountService;

    private FirebaseApp firebaseApp;

    private void firebaseInitialization() throws IOException {
        FileInputStream serviceAccount =
                (FileInputStream) new URL("https://s3.ap-east-1.amazonaws.com/test.howard.gnil/fyp/fyp2020-e4f03-firebase-adminsdk-lx3fd-92e24818ed.json").openStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fyp2020-e4f03.firebaseio.com")
                .build();
        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public Result<Long> login(String token){
        if(firebaseApp == null && token!="000"){
            try{
                firebaseInitialization();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
        Result<Long> result = new Result<>();

        //<Testing Admin access>
        if(token == "000"){
            User u = userRepository.getByFirebaseUid("AVEO2GefpydRxMLmKGzPX8ERjEV2").get();
            if(u == null){
                u = new User();
                u.setEmail("fyp2020group10@gmail.com");
                u.setFirebaseUid("AVEO2GefpydRxMLmKGzPX8ERjEV2");
                addUser(u);
            }
            result.setCode(0);
            result.setData(u.getId());
            return result;
        }
        //</Testing Admin access>

        try{
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();
            User u = userRepository.getByFirebaseUid(uid).get();
            if(u == null){
                UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
                u = new User();
                u.setEmail(userRecord.getEmail());
                u.setFirebaseUid(uid);
                addUser(u);
            }
            result.setCode(0);
            result.setData(u.getId());
            return result;
        }
        catch(FirebaseAuthException ex)
        {
            result.setCode(1);
            result.setMsg("Wrong token");
        }
        return result;
    }

    @Transactional
    public Result<User> addUser(User user){
        Result<User> result = new Result<>();
        User u = userRepository.save(user);
        if(u != null){
            result.setCode(0);
            result.setData(u);
        }else{
            result.setCode(-1);
            result.setMsg("error");
        }
        return result;
    }


    public Long countAll() {
        return userRepository.countAll();
    }

    /**
     * find one user
     * @param id
     * @return
     */
    public User find(Long id){
        Assert.notNull(id, "required user id");
        LOG.debug("find one user, id={}", id);
        User user = userRepository.findById(id).orElse(null);
        Assert.notNull(user, "user not exits");
        LOG.debug("find one user, result={}", user);
        return user;
    }

    /**
     *
     */
    public List<Account> getAllAccount(Long id){
        Assert.notNull(id, "required user id");
        LOG.debug("find one user, id={}", id);
        User user = userRepository.findById(id).orElse(null);
        List<Account> accounts = user.getAccountList();
        LOG.debug("find one user, result={}", accounts);
        return accounts;
    }

    @Transactional
    public Result<User> addUser(Long id){
        User user = new User();
        user.setFirebaseUid("test");
        user.setId(id);
        return addUser(user);
    }

    @Transactional
    public Result<User> addUserInfo(UserInfo userInfo, Long id){
        User user = userRepository.findById(id).orElse(null);
        Assert.notNull(user, "user not exits");
        if (user.getUserInfo()!=null) return ResultUtil.error(-1,"user info already set");
        userInfoService.addUserInfo(userInfo);
        LOG.debug("add user info, user info={}",userInfo);
        user.setUserInfo(userInfo);
        return addUser(user);
    }

    @Transactional
    public Result<User> addAccount(Account account, Long id){
        User user = userRepository.findById(id).orElse(null);
        Assert.notNull(user, "user not exits");
        accountService.setAccount(account);
        LOG.debug("add account, account={}",account);
        for(Account a:user.getAccountList()){
            if(a.getCategory().equals(account.getCategory()))
                return ResultUtil.error(-1,"account already exists");
        }
        user.getAccountList().add(account);
        return addUser(user);
    }

}
