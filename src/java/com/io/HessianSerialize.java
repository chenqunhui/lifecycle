package com.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianSerialize implements Serialize {

	@Override
	public byte[] serialize(Object obj) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		try{
			ho.writeObject(obj);
			return os.toByteArray();
		} catch (IOException e) {
			return null;
		}finally{
			try {
				ho.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Object deserialize(byte[] bytes, Class<?> clazz) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		HessianInput in = new HessianInput(is);
		try {
			return in.readObject(clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			in.close();
		}
		
	}

}
