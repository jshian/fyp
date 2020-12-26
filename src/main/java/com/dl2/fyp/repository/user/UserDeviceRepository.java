package com.dl2.fyp.repository.user;

import com.dl2.fyp.entity.UserDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends CrudRepository<UserDevice, Long> {
}
