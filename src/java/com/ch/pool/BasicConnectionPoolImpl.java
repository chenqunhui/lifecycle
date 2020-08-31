package com.ch.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.ch.net.Connection;
import com.ch.net.ConnectionPool;

public class BasicConnectionPoolImpl implements ConnectionPool {

	GenericObjectPool<Connection> pool;
	
	public BasicConnectionPoolImpl(ConnectionFactory factory,GenericObjectPoolConfig config){
		pool = new GenericObjectPool<Connection>(factory,config);
	}

	@Override
	public Connection getConnection(){
		try {
			return pool.borrowObject(3000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void returnConnection(Connection conn) {
		pool.returnObject(conn);
	}

}
