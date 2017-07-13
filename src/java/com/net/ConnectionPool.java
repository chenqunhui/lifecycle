package com.net;

public interface ConnectionPool {

	Connection getConnection();
	
	void returnConnection(Connection conn);
}
