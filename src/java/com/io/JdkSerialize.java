package com.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JdkSerialize implements Serialize {

	@Override
	public byte[] serialize(Object obj) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(stream);
			outputStream.writeObject(obj);
			return stream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			if(null != outputStream){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Object deserialize(byte[] bytes, Class<?> clazz) {
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		ObjectInputStream oi  = null;
		try {
			oi = new ObjectInputStream(input);
			try {
				return oi.readObject();
			} catch (ClassNotFoundException e) {
			}
		} catch (IOException e) {
			
		}finally{
			try {
				if(null != oi) 
					oi.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

}
