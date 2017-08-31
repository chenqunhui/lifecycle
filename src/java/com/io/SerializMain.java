package com.io;

public class SerializMain {
	
	public static void main(String[] args) {
		JdkSerialize jdk = new JdkSerialize();
		HessianSerialize hessian = new HessianSerialize();
		Hessian2Serialize hessian2 = new Hessian2Serialize();
		KryoSerialize kryo = new KryoSerialize();
		FstSerialize fst = new FstSerialize();
		
		ptl("		大小		序列化	  	反序列化");
		byte[] bytes = null;
		long st = System.currentTimeMillis();
		//JDK
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			bytes = jdk.serialize(user);
		}
		pt("JDK 		"+bytes.length+"		"+(System.currentTimeMillis()-st)+"ms		");
		st= System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			jdk.deserialize(bytes, null);
		}
		ptl((System.currentTimeMillis()-st)+"ms");
		
		//=====hessian
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			bytes = hessian.serialize(user);
		}
		pt("Hessian 	"+bytes.length+"		"+(System.currentTimeMillis()-st)+"ms		");
		st= System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			hessian.deserialize(bytes, User.class);
		}
		ptl((System.currentTimeMillis()-st)+"ms");
		
		//=====hessian2
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			bytes = hessian2.serialize(user);
		}
		pt("Hessian2 	"+bytes.length+"		"+(System.currentTimeMillis()-st)+"ms		");
		st= System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			hessian2.deserialize(bytes, User.class);
		}
		ptl((System.currentTimeMillis()-st)+"ms");
		//=====kryo
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			bytes = kryo.serialize(user);
		}
		pt("Kryo 		"+bytes.length+"		"+(System.currentTimeMillis()-st)+"ms		");
		st= System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			kryo.deserialize(bytes, User.class);
		}
		ptl((System.currentTimeMillis()-st)+"ms");
		
		//=====fst
		for(int i=0;i<1000000;i++){
			User user = new User("the name"+i);
			bytes = fst.serialize(user);
		}
		pt("Fst 		"+bytes.length+"		"+(System.currentTimeMillis()-st)+"ms		");
		st= System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			fst.deserialize(bytes, null);
		}
		ptl((System.currentTimeMillis()-st)+"ms");
		
	}
	
	
	public static void pt(Object obj){
		System.out.print(obj);
	}

	public static void ptl(Object obj){
		System.out.println(obj);
	}
}
