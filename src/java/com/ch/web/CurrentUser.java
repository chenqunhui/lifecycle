package com.ch.web;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CurrentUser {

    private Long Id;
    private String userName;
    private String mobile;
    private String email;
    private String token;

}
