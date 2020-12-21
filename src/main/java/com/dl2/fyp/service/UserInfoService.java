package com.dl2.fyp.service;

import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.UserInfoRepository;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * create user info
     * @param userInfo
     * @return
     */
    public UserInfo addUserInfo(UserInfo userInfo){
        LOG.debug("create user info, parameter:{}", userInfo);

        checkInfo(userInfo);

        setDefault(userInfo);

        UserInfo result = userInfoRepository.save(userInfo);
        LOG.debug("create user info, result:{}",result);
        return result;
    }

    /**
     * update user info
     * @param userInfo
     * @return
     */
    public UserInfo updateUserInfo(UserInfo userInfo){
        LOG.debug("create user info, parameter:{}", userInfo);

        UserInfo result = userInfoRepository.save(userInfo);
        LOG.debug("create user info, result:{}",result);
        return result;
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
