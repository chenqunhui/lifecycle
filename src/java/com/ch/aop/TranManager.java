package com.ch.aop;

public class TranManager {

	public void beginTran(){
		System.out.println("begin");
	}
	
	public void commit(){
		System.out.println("commit");
	}
}
