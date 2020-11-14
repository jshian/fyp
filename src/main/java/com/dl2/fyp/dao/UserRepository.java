package com.dl2.fyp.dao;

import com.dl2.fyp.entity.User;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select User from User where User.firebaseUid = ?1", nativeQuery = true)
    User findByFirebaseUid(String FirebaseUid);


//    User insert(User userToAdd);

    @Query(value = "select User from User where User.firebaseUid = ?1", nativeQuery = true)
    Optional<User> getByFirebaseUid(String FirebaseUid);


    @Query(value = "select count(User.id) from User", nativeQuery = true)
    public Long countAll();

}
