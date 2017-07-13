package com.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.net.Connection;
import com.net.tcp.TcpConnection;

public class TcpConnectionFactory extends ConnectionFactory {

	@Override
	public Connection create() throws Exception {
		return new TcpConnection();
	}

	@Override
	public PooledObject<Connection> wrap(Connection obj) {
		return new DefaultPooledObject<Connection>(obj);
	}

}
