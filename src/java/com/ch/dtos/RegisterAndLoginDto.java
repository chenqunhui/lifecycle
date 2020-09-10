package com.ch.dtos;

import lombok.Data;

@Data
public class RegisterAndLoginDto extends BaseDto {

    private String mobile;
    private String verificationCode;

}
