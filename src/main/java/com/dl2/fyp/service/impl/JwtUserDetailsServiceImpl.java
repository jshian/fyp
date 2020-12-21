package com.dl2.fyp.service.impl;

import com.dl2.fyp.repository.UserRepository;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.domain.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByFirebaseUid(username);

        if(user == null){
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.",username));
        }else{
            return JwtUserFactory.create(user);
        }
    }
}
