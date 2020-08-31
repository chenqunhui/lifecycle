package com.ch.net;

public interface Connection {

	public void connect();
	
	public boolean connected();
	
	public void close();
	
	public boolean closed();
	
	
}
