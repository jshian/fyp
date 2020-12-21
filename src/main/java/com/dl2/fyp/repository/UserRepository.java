package com.dl2.fyp.repository;

import com.dl2.fyp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, CrudRepository<User, Long> {

    @Query(value = "select User from User where User.firebaseUid = ?1", nativeQuery = true)
    User findByFirebaseUid(String FirebaseUid);


//    User insert(User userToAdd);

    @Query(value = "select User from User where User.firebaseUid = ?1", nativeQuery = true)
    Optional<User> getByFirebaseUid(String FirebaseUid);


    @Query(value = "select count(User.id) from User", nativeQuery = true)
    public Long countAll();

}
