package com.dl2.fyp.repository;

import com.dl2.fyp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select distinct count(User.id) from User")
    public Integer countAll();
}
