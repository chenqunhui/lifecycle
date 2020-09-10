package com.ch.dao;

import com.ch.model.VerificationCode;
import org.apache.ibatis.annotations.Param;

public interface VerificationCodeDao {

    VerificationCode getByMobileAndBusinessType(@Param("mobile") String mobile,@Param("businessType") String businessType);

    Long insert(VerificationCode code);

    void invalid(long id);
}
