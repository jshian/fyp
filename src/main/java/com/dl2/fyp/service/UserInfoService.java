package com.dl2.fyp.service;

import com.dl2.fyp.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * user info service
 */
@Service
public class UserInfoService {
    private static Logger LOG = LoggerFactory.getLogger(UserInfoService.class);

    /**
     * create user info
     * @param userInfo
     * @return
     */
    public UserInfo addUserInfo(UserInfo userInfo){
        LOG.debug("create user info, parameter:{}", userInfo);

        checkInfo(userInfo);

        setDefault(userInfo);

//        UserInfo result = userInfoRepository.save(userInfo);
        LOG.debug("create user info, result:{}",userInfo);
        return userInfo;
    }

    /**
     * update user info
     * @param oldInfo
     * @param newInfo
     * @return
     */
    public UserInfo updateUserInfo(UserInfo oldInfo, UserInfo newInfo){
        LOG.debug("create user info, parameter:{}", oldInfo);
        if (newInfo.getAge()!=null) oldInfo.setAge(newInfo.getAge());
        if (newInfo.getAcceptableRisk()!=null) oldInfo.setAcceptableRisk(newInfo.getAcceptableRisk());
        if (newInfo.getMaritalStatus()!=null) oldInfo.setMaritalStatus(newInfo.getMaritalStatus());
        LOG.debug("create user info, result:{}",oldInfo);
        return oldInfo;
    }

    /**
     * set the default value
     * @param userInfo
     */
    private void setDefault(UserInfo userInfo) {

    }

    /**
     * check the info data
     * @param userInfo
     */
    private void checkInfo(UserInfo userInfo) {

    }


}
