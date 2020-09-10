package com.ch.service;

import com.ch.dao.OperateLogDao;
import com.ch.dao.UserInfoDao;
import com.ch.exceptions.BusinessException;
import com.ch.model.*;
import com.ch.web.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private OperateLogDao loginLogDao;

    public UserInfo register(String mobile){
        UserInfo exists = userInfoDao.getByMobile(mobile);
        if(null != exists){
            throw new BusinessException("");
        }
        UserInfo userInfo = UserInfo.builder()
                .userName(mobile)
                .mobile(mobile)
                .password(randomPassword(mobile))
                .createdTime(new Date())
                .updatedTime(new Date())
                .build();
        try{
            userInfoDao.insert(userInfo);
        }catch (DuplicateKeyException e){
            throw new BusinessException("");
        }
        return userInfo;
    }


    private String randomPassword(String mobile){
        //todo
        return mobile;
    }

    private String randomToken(String mobile){
        //TODO
        return "125151512";
    }

    /**
     * 注册登录|登录
     * @param mobile
     * @return
     */
    public UserInfo registAndLogin(String mobile) {
        UserInfo userInfo = userInfoDao.getByMobile(mobile);
        if (null == userInfo) {
            userInfo = UserInfo.builder()
                    .userName(mobile)
                    .mobile(mobile)
                    .password(randomPassword(mobile))
                    .createdTime(new Date())
                    .updatedTime(new Date())
                    .isValid(1)
                    .token(randomToken(mobile))
                    .build();
            userInfoDao.insert(userInfo);
        } else {
            String token = randomToken(mobile);
            userInfo.setToken(token);
            userInfoDao.updateToken(userInfo.getId(), token);
        }
        return userInfo;
    }


    /**
     * 通过token查询用户信息
     * @param token
     * @return
     */
    public CurrentUser getUserInfoByToken(String token) {
        UserInfo userInfo=  userInfoDao.getByToken(token);
        if(null == userInfo){
            return null;
        }else{
            CurrentUser currentUser = new CurrentUser();
            BeanUtils.copyProperties(userInfo,currentUser);
            return currentUser;
        }
    }

    public void loginOut(Long userId){
        userInfoDao.updateToken(userId,null);
    }
}
