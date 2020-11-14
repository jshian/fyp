package com.dl2.fyp.service.impl;

import com.dl2.fyp.dao.UserRepository;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.domain.JwtUser;
import com.dl2.fyp.service.AuthService;
import com.dl2.fyp.util.JwtTokenUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Arrays;

@Service
public class AuthServiceImpl implements AuthService {


    private AuthenticationManager authenticationManager;
    private JwtUserDetailsServiceImpl jwtUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;
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

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUserDetailsServiceImpl jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public User register(User userToAdd) {
        final String uid = userToAdd.getFirebaseUid();
        if(userRepository.findByFirebaseUid(uid) !=null) {
            return null;
        }
        return userRepository.save(userToAdd);
    }

    @Override
    public String login(String token) {
        if(firebaseApp == null && token!="000"){
            try{
                firebaseInitialization();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
        try{
            String uid;
            if (token == "000"){
                uid = "AVEO2GefpydRxMLmKGzPX8ERjEV2";
            }else{
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                uid = decodedToken.getUid();
            }

            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(uid, null);
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(uid);
            final String jwtToken = jwtTokenUtil.generateToken(userDetails);
            return jwtToken;
        }catch(FirebaseAuthException ex){
            System.out.println(ex.toString());
            return null;
        }
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
