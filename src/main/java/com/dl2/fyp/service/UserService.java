package com.dl2.fyp.service;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;


@Service
public class UserService{
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

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
                add(u);
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
                add(u);
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

    public Result<User> add(User user){
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
        LOG.debug("find one user, result={}", user);
        return user;
    }

    /**
     * create user
     * @param id
     * @return
     */
    public void add(Long id){
        User user = new User();
        user.setId(id);
        user.setFirebaseUid("hello world");
        userRepository.save(user);

    }

}
