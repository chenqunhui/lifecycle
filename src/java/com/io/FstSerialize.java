package com.io;

import org.nustaq.serialization.FSTConfiguration;

public class FstSerialize implements Serialize {

	static FSTConfiguration configuration = FSTConfiguration.createStructConfiguration();
	
	@Override
	public byte[] serialize(Object obj) {
		return configuration.asByteArray(obj);
	}

	@Override
	public Object deserialize(byte[] bytes, Class<?> clazz) {
		return configuration.asObject(bytes);
	}

}
