package com.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.net.Connection;
import com.net.ConnectionPool;

public class TestPool {
	public static void main(String[] args){
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(10);
		ConnectionPool tcpPool = new BasicConnectionPoolImpl(new TcpConnectionFactory(),config);
		try {
			Connection conn;
			for(int i=0;i<100;i++){
				 conn = tcpPool.getConnection();
				pt(conn);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	static void pt(Object o){
		System.out.println(o.hashCode());
	}
}
