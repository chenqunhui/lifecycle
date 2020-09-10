package com.ch.controller;

import com.ch.service.VerificationCodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "验证码相关接口")
@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

//    @PostMapping("/send")
//    public VerificationCode sendVerificationCode(String mobile,@RequestParam String businessType){
//        return verificationCodeService.createVerificationCode(mobile,businessType);
//    }
}
