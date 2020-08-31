package com.ch.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkClientFactory {
	 private static CuratorFramework client;
	 //构造全局的zk链接对象
	 public static CuratorFramework  getInstance(String url){
		 if(null == client){
			 synchronized(ZkClientFactory.class){
				 if(null == client){
					 CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();  
				        client = builder.connectString(url) 
				        		.sessionTimeoutMs(3000)  
				                .connectionTimeoutMs(3000)  
				                .canBeReadOnly(false)  
				                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
				                .namespace(null)  
				                .defaultData(null)  
				                .build();  
				        client.start();
				 }
			 }
		 }
		 return client;
	 }
}
