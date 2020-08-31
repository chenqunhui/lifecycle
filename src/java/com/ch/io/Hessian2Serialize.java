package com.ch.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class Hessian2Serialize implements Serialize {

	@Override
	public byte[] serialize(Object obj) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Hessian2Output ho = new Hessian2Output(os);
		try{
			ho.writeObject(obj);
			ho.flushBuffer();
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
		Hessian2Input in = new Hessian2Input(is);
		try {
			return in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
