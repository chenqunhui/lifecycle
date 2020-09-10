package com.ch.service;

import com.ch.dao.VerificationCodeDao;
import com.ch.enums.VerifactionCodeBusinessType;
import com.ch.model.VerificationCode;
import com.ch.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VerificationCodeService {

    @Autowired
    private VerificationCodeDao verificationCodeDao;

    public VerificationCode getByMobileAndBusinessType(String mobile,VerifactionCodeBusinessType businessType){
        return verificationCodeDao.getByMobileAndBusinessType(mobile,businessType.name());
    }

    public  VerificationCode createVerificationCode(VerifactionCodeBusinessType businessType,String mobile){
        Date now = new Date();
        VerificationCode code = VerificationCode.builder()
                .businessType(businessType.name())
                .mobile(mobile)
                .verificationCode(randomVerificationCode())
                .createdTime(now)
                .expiredTime(DateUtils.addMinutes(now,businessType.expireMinutes))
                .updatedTime(now)
                .isValid(1)
                .build();
        verificationCodeDao.insert(code);
        return code;
    }

    private String randomVerificationCode(){
        return "000000";
    }


    public void invalid(long id){
        verificationCodeDao.invalid(id);
    }

}
