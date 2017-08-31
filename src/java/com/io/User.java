package com.io;

import java.io.Serializable;
import java.util.Date;

import com.net.ConnectionPool;
import com.utils.Sqlmap2Table;

public class User implements Serializable{

	private static final long serialVersionUID = -8231153923233486757L;
	
	private String name;
	
	private User user ;
	
	private Date date = new Date();
	
	private double id = Math.random();
	
	private transient ConnectionPool connectionPool;
	
	private Sqlmap2Table sqlmap2Table = new Sqlmap2Table();
	
	public User(){
		
	}
	
	public User(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	
	
}
