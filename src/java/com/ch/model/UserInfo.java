package com.ch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo implements Serializable {

    private Long Id;
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private Date createdTime;
    private Date updatedTime;
    private Integer isValid;
    private String token;

}
