package com.io;

public interface Serialize {

	
	byte[] serialize(Object obj);
	
	Object deserialize(byte[] bytes,Class<?> clazz);
	
	
}
