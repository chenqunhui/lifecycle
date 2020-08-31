package com.ch.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.ch.net.Connection;
import com.ch.net.tcp.TcpConnection;

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
