package com.ch.enums;

import lombok.Data;

public enum  VerifactionCodeBusinessType {


    LOGIN(5,"登录");




    public int expireMinutes; //过期时间：分情
    public String desc;

    VerifactionCodeBusinessType(int expireMinutes,String desc){
        this.expireMinutes = expireMinutes;
        this.desc = desc;
    }

}
