package com.ch.enums;

public enum BusinessErrorCode {


    user_dumplicate("001","用户已存在");












    private String code;
    private String errMsg;

    BusinessErrorCode(String code,String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }





}
