package com.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkHandler implements InvocationHandler {  
	  
    private Object target;  //被代理对象 需要注入  
      
    private TranManager tranManager = new TranManager(); //包含需要织入代码的对象，如事务管理器  
      
    public JdkHandler(Object target){  
        this.target = target;  
    }  
    @Override  
    public Object invoke(Object proxy, Method method, Object[] args)  
            throws Throwable {  
        System.out.println(proxy.getClass());  
        tranManager.beginTran(); //动态织入代码  
        Object o = method.invoke(target, args); //通过反射调用被代理的方法  
        tranManager.commit();   //动态织入代码  
        return o;  
    }  
    
    public static void main(String[] args){
        JdkHandler jdkHander = new JdkHandler(new JdkClass());  
         //在这里可以看到，通过Proxy的newProxyInstance创建代理对象时，第二个参数是接口  
        JdkInterface d = (JdkInterface) Proxy.newProxyInstance(JdkClass.class.getClassLoader(),
        		JdkClass.class.getInterfaces(), jdkHander);
        d.todo();
    }
}