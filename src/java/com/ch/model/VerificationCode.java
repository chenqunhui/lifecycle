package com.ch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VerificationCode implements Serializable{

    private Long id;
    private String businessType;
    private String verificationCode;
    private String mobile;
    private Date createdTime;
    private Date expiredTime;
    private Date updatedTime;
    private Integer isValid; //是否有效，1：有效，0：失效


    /**
     * 是否可用
     * @return
     */
    public boolean canBeUsed(){
        if(null == isValid || isValid != 1){
            return false;
        }
        return null == expiredTime ? true : expiredTime.after(new Date());
    }

}
