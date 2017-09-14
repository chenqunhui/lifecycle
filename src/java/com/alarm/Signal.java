package com.alarm;

import java.util.Date;

import com.utils.DateUtils;
import com.utils.IpUtils;


public class Signal {

	Date time = new Date();  //报警时间
	
	String host = IpUtils.getLocalHostIP();     //机器ip
	
	String method;   //方法名称
	
    boolean isNomal;
	
	String info;
	
/*	public Signal(){
		
	}*/
	
	public Signal(String method,boolean isNomal,String info){
		this.method = method;
		this.info = info;
		this.isNomal = isNomal;
	}
	
	public String getKey(){
		return new StringBuilder().append(host).append("_").append(method).toString();
	}

	
	public String toString(){
    	return new StringBuilder().append(DateUtils.format(time, "yyyy-MM-dd HH:mm:ss")).append(" ")
    			.append("ip:").append(host).append(" ")
    			.append("方法:").append(method)
    			.append(info).toString();
    }
	
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}


	public boolean isNomal() {
		return isNomal;
	}

	public void setNomal(boolean isNomal) {
		this.isNomal = isNomal;
	}
}
