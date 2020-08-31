/** 
 * Project Name:advertise-common <br>
 * File Name:AlarmMessage.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.notify;

/**
 * @author wayne
 * @date 2017年7月28日 上午9:55:32
 */
public class NotifyMessage {
	
	private AlarmType alarmType;
	
	private String title;
	
	private String content;
	
	private String[] receivers;
	
	public NotifyMessage() {
		super();
	}

	public NotifyMessage(AlarmType alarmType, String title, String content, String[] receivers) {
		super();
		this.alarmType = alarmType;
		this.title = title;
		this.content = content;
		this.receivers = receivers;
	}

	public AlarmType getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(AlarmType alarmType) {
		this.alarmType = alarmType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getReceivers() {
		return receivers;
	}

	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}

	public enum AlarmType{
		SMS, MAIL
	}
}
