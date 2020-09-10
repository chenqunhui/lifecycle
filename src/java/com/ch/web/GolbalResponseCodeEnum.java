package com.ch.web;

public enum GolbalResponseCodeEnum {

	SUCCESS(200),
	FAIL(900,"操作失败"),
	EXCEPTION(999,"系统异常");
	
	
	
	private int code;
	private String desc;
	
	GolbalResponseCodeEnum(int code){
		this.code = code;
	}
	
	GolbalResponseCodeEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	
	public int getCode(){
		return code;
	}
	
	public String getDesc(){
		return desc;
	}
	
}
