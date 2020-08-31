package com.ch.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerialize  implements Serialize{

	private Kryo kryo = new Kryo(); 
	
	public KryoSerialize(){
		kryo.register(User.class); 
	}
	
	@Override
	public byte[] serialize(Object obj) {
		//
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		Output output = new Output(outStream, 4096); 
		try{
			kryo.writeObject(output, obj); 
		}finally{
			output.close();
		}
		return outStream.toByteArray();
	}

	@Override
	public Object deserialize(byte[] bytes,Class<?> clazz) {
		Input input = new Input(new ByteArrayInputStream(bytes),4096);
		try{
			return kryo.readObject(input, clazz);
		}finally{
			input.close();
		}
	}

}
