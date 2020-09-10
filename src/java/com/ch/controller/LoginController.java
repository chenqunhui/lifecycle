package com.ch.controller;

import com.ch.dtos.RegisterAndLoginDto;
import com.ch.dtos.ThirdPartyLoginDto;
import com.ch.dtos.UserPasswordLoginDto;
import com.ch.enums.VerifactionCodeBusinessType;
import com.ch.exceptions.BusinessException;
import com.ch.model.*;
import com.ch.service.UserInfoService;
import com.ch.service.VerificationCodeService;
import com.ch.web.CurrentUser;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "用户登录")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/sendLoginVerificationCode")
    public VerificationCode sendLoginVerificationCode(@RequestParam String mobile){
        return  verificationCodeService.createVerificationCode(VerifactionCodeBusinessType.LOGIN,mobile);
    }



    /**
     * 验证码 注册|登录
     * @param registerAndLoginDto
     */
    @PostMapping("/registerAndLogin")
    public String registerAndLogin(RegisterAndLoginDto registerAndLoginDto){
        VerificationCode code = verificationCodeService.getByMobileAndBusinessType(registerAndLoginDto.getMobile(), VerifactionCodeBusinessType.LOGIN);
        if(null == code || !code.getVerificationCode().equals(registerAndLoginDto.getVerificationCode())){
            throw new BusinessException("验证码错误");
        }else if(!code.canBeUsed()){
            throw new BusinessException("验证码已过期");
        }else{
            verificationCodeService.invalid(code.getId());
            UserInfo info = userInfoService.registAndLogin(registerAndLoginDto.getMobile());
            return info.getToken();
        }
    }

    /**
     * 获取当前用户信息
     * @param currentUser
     * @return
     */
    @GetMapping("/userInfo")
    public CurrentUser userInfo(CurrentUser currentUser){
        return currentUser;
    }

//    /**
//     * 密码登录
//     * @param loginInfo
//     * @return
//     */
//    public LoginStatus loginWithPw(UserPasswordLoginDto loginInfo){
//        return null;
//    }
//
//
//
//
    @PostMapping("/loginOut")
    public void loginOut(CurrentUser currentUser){
        userInfoService.loginOut(currentUser.getId());
    }


}
