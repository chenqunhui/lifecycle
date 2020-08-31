package com.ch.aop;





import net.sf.cglib.proxy.Enhancer;

public class CglibProxy {  
    public static void main(String[] args){
    	Enhancer enhancer = new Enhancer();
    	enhancer.setSuperclass(JdkClass.class); // ① 设置需要创建子类的类  
        enhancer.setCallback(new TranCallback()); 
        JdkClass c = (JdkClass)enhancer.create();
    	c.todo();
    }
} 