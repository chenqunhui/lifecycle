package com.ch.zk;

public class TestSync {

	public static void main(String[] args) {

		Job j1= new Job();
		Job j2= new Job();
		j1.start();
		j2.start();
	}

}



class Job extends Thread{
	public void run(){
		synchronized(this){
			System.out.println(this.getName()+"....1");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(this.getName()+"....2");
		}
	}
	
}