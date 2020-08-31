package com.ch.zk;

import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class Lock {
  	private static final String ZK_ADDRESS = "192.168.62.220:2182";
  	
    private static CuratorFramework client = ZkClientFactory.getInstance(ZK_ADDRESS);
    
    public static void main(String[] args){
    	MyJob jb1 = new MyJob(client);
    	MyJob jb2 = new MyJob(client);
    	MyJob jb3 = new MyJob(client);
    	jb1.start();
    	jb2.start();
    	jb3.start();
    }

}


class MyJob extends Thread{
	CuratorFramework client;
	
	public MyJob(CuratorFramework client){
		this.client = client;
	}
	public void run(){
		InterProcessMutex lock = new InterProcessMutex(client, "/test");
    	try {
			//lock.acquire();//会一直阻塞到获得锁成功
    		boolean locked= lock.acquire(2000, TimeUnit.MILLISECONDS);//获得锁成功或阻塞一定时间返回
    		if(locked){
    			System.out.println(this.getName()+"获取锁成功");
    			Thread.sleep(3000);
    		}else{
    			System.out.println(this.getName()+"获取锁失败");
    		}
		} catch (Exception e) {

			
		}finally{
			try {
				lock.release();
				System.out.println(this.getName()+"释放锁成功");
			} catch (Exception e) {
			}
		}
	}
}
