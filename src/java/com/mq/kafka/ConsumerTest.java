package com.mq.kafka;

 
public class ConsumerTest{
    
	public static void main(String[] args){
		String str = "UserName=bbsbj${and}UserPass=baobaoshu!57${and}Mobile=${receiver}${and}Content=${content}";
		while(str.contains("${and}"))
		str = str.replace("${and}", "&");
		
		System.out.println(str);
	}
	
	
	
}