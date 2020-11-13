package com.dl2.fyp.service;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;


    public Result<Long> login(String username, String password){
        Result<Long> result = new Result<>();
        User u = userRepository.getByUsername(username).get();
        if(u == null){
            result.setCode(-1);
            result.setMsg("User not found");
        }else if(u.getPassword().equals(password)){
            System.out.println(u.toString());
            result.setCode(0);
            result.setData(u.getId());
        }else{
            result.setCode(1);
            result.setMsg("Wrong password");
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

}
