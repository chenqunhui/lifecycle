package com.ch.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TranCallback implements MethodInterceptor{

	
	//被织入的事务对象
	private TranManager tranManager = new TranManager();
	
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    	tranManager.beginTran();  
        Object result = proxy.invokeSuper(obj, args);  
        tranManager.commit();  
        return result;  
	}



}
