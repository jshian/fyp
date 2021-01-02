package com.dl2.fyp.dao;

import com.dl2.fyp.entity.User;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select User from User where User.username = ?1")
    User findByUsername(String username);


//    User insert(User userToAdd);

    @Query("select User from User where User.username = ?1")
    Optional<User> getByUsername(String username);


    @Query("select distinct count(User.id) from User")
    public Long countAll();

}
