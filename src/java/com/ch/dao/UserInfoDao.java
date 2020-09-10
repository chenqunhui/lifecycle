package com.ch.dao;

import com.ch.model.UserInfo;
import com.ch.web.CurrentUser;
import org.apache.ibatis.annotations.Param;

public interface UserInfoDao {

    UserInfo getByMobile(String mobile);

    UserInfo getByToken(String token);

    Long insert(UserInfo userInfo);

    int updateToken(@Param("id") Long userId ,@Param("token") String token);

    int updateValid(@Param("id") Long userId ,@Param("isValid") int isValid);



}
