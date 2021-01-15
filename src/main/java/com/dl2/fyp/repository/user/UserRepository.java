package com.dl2.fyp.repository.user;

import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.enums.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, CrudRepository<User, Long>{

    @Query(value = "select * from t_user where firebase_uid = ?1", nativeQuery = true)
    User findByFirebaseUid(String FirebaseUid);

    @Query(value = "select * from t_user where firebaseUid = ?1", nativeQuery = true)
    Optional<User> getByFirebaseUid(String FirebaseUid);

    @Query(value = "select count(id) from t_user", nativeQuery = true)
    Long countAll();

}
