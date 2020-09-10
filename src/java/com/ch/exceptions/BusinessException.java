package com.ch.exceptions;

import lombok.Data;

/**
 * 	业务异常
 *
 */

@Data
public class BusinessException extends RuntimeException{
	private static final long serialVersionUID = 6118874524492682160L;

	private String alertMsg;

	private int errorCode = 999;
	
	public BusinessException(int errorCode, String alertMsg){
		super(alertMsg);
		this.errorCode = errorCode;
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(int errorCode, String alertMsg, String errMsg){
		super(errMsg);
		this.errorCode = errorCode;
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg){
		super(alertMsg);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg, String errMsg){
		super(errMsg);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg, String errMsg, Throwable e){
		super(errMsg,e);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(String alertMsg, Throwable e){
		super(alertMsg,e);
		this.alertMsg = alertMsg;
	}
	
	public BusinessException(Throwable e){
		super(e);
	}


}
