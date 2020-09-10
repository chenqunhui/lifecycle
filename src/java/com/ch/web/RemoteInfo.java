package com.ch.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RemoteInfo {
    private String ip; // ip
    private String mac;//mac地址
    private String imei;  //手机串码
    private String token; //
    private String traceId; //
}
