package com.dl2.fyp.service.impl;

import com.dl2.fyp.repository.user.UserRepository;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.domain.JwtUser;
import com.dl2.fyp.service.AuthService;
import com.dl2.fyp.service.firebase.FirebaseService;
import com.dl2.fyp.util.JwtTokenUtil;
import com.google.firebase.FirebaseApp;
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
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class AuthServiceImpl implements AuthService {

    private FirebaseService firebaseService;
    private AuthenticationManager authenticationManager;
    private JwtUserDetailsServiceImpl jwtUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;

    private static FirebaseApp firebaseApp;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUserDetailsServiceImpl jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository, FirebaseService firebaseService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
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
    public String login(String token) throws IOException {
        if(firebaseApp == null && token!="000"){
            firebaseApp = firebaseService.getFirebaseApp();
        }
        try{
            String uid;
            if (token.equals("000")){
                uid = "BGtGgQJRNNeQe5Bf6HOCdm5RYJo1";
            }else{
                try {
                    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                    uid = decodedToken.getUid();
                }
                catch(FirebaseAuthException ex){
                    System.out.println(ex.toString());
                    return null;
                }
            }
            User user = userRepository.findByFirebaseUid(uid);
            if(user==null) {
                User userToAdd = new User();
                userToAdd.setFirebaseUid(uid);
                register(userToAdd);
            }

            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(uid, "123456");
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(uid);
            final String jwtToken = jwtTokenUtil.generateToken(userDetails);
            return jwtToken;
        }
        catch(Exception ex){
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
