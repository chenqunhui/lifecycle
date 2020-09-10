package com.ch.web;

import com.ch.exceptions.BusinessException;
import com.ch.log.TraceLogIdUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * http/https通用返回值
 */
@Data
@ToString
public class GlobalResponse<T> {

	
    @ApiModelProperty(notes = "业务成功时:true,失败时：false")
    protected boolean success = true;
    @ApiModelProperty(notes = "可以用来指定特定的错误原因")
    private Integer errorCode;
    @ApiModelProperty(notes = "数据")
    private T data;
    @ApiModelProperty(notes = "用来弹窗展示的错误信息，如果不为空，展示给用户")
    private String alertMsg;
    @ApiModelProperty(notes = "日志跟踪号")
    protected String traceLogId;
    

    public GlobalResponse() {
    	
    }

    public GlobalResponse(T data, String traceLogId) {
    	this.traceLogId = traceLogId;
        this.data = data;
        this.alertMsg="操作成功";
    }

    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(data, TraceLogIdUtils.getCurrentThreadTraceLogId());
    }
    
    public static <T> GlobalResponse<T> fail(String alertMsg){
    	GlobalResponse<T> resp = new GlobalResponse<T>();
    	resp.setTraceLogId(TraceLogIdUtils.getCurrentThreadTraceLogId());
    	resp.setAlertMsg(StringUtils.isEmpty(alertMsg)? GolbalResponseCodeEnum.FAIL.getDesc() :alertMsg);
    	resp.setErrorCode(999);
    	resp.setSuccess(false);
        return resp;
    }
    
    public static <T> GlobalResponse<T> exception(Throwable e,String alertMsg) {
    	GlobalResponse<T> resp = new GlobalResponse<T>();
    	resp.setTraceLogId(TraceLogIdUtils.getCurrentThreadTraceLogId());
    	resp.setAlertMsg(StringUtils.isEmpty(alertMsg)? GolbalResponseCodeEnum.EXCEPTION.getDesc() :alertMsg);
    	resp.setErrorCode(999);
        resp.setSuccess(false);
        return resp;
    }
    
    public static <T> GlobalResponse<T> businessFail(BusinessException e) {
    	GlobalResponse<T> resp = new GlobalResponse<T>();
    	resp.setErrorCode(e.getErrorCode());
    	resp.setTraceLogId(TraceLogIdUtils.getCurrentThreadTraceLogId());
    	resp.setAlertMsg(StringUtils.isEmpty(e.getAlertMsg())? GolbalResponseCodeEnum.EXCEPTION.getDesc() :e.getAlertMsg());
        resp.setSuccess(false);
        return resp;
    }
    
    public static <T> GlobalResponse<T> httpError(Integer httpCode) {
    	Integer code = null == httpCode? GolbalResponseCodeEnum.EXCEPTION.getCode() : httpCode;
    	GlobalResponse<T> resp = new GlobalResponse<T>();
    	resp.setTraceLogId(TraceLogIdUtils.getCurrentThreadTraceLogId());
    	resp.setAlertMsg(GolbalResponseCodeEnum.EXCEPTION.getDesc());
        resp.setSuccess(false);
        return resp;
    }
}
