package com.ch.model;

import com.ch.dtos.BaseDto;
import com.ch.web.RemoteInfo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
public class OperateLog  implements Serializable{


    private Long id;
    private String path;
    private String traceId;
    private String input;
    private String output;
    private RemoteInfo remoteInfo;
    private Date createdTime;

}
