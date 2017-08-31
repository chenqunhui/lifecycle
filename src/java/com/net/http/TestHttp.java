package com.net.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class TestHttp {

	
	public static void main(String[] args){
		
		String url = "http://localhost:8080/user/login.do";
		for(int i=0;i<20;i++){
			new MyJob(url).start();
		}
		
        
		
		
	}
	
	static class MyJob extends Thread{
		
		public MyJob(String url){
			this.url = url;
		}
		
		private String url;
		
		public void run(){
		
	        // 通过请求对象获取响应对象
	        try {
	        	for(int i=0;i<100;i++){
	        		HttpGet request = new HttpGet(url);//这里发送get请求
	    	        // 获取当前客户端对象
	    	        HttpClient httpClient = new DefaultHttpClient();
	        		HttpResponse response = httpClient.execute(request);
	        		request.abort();
	        	}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
}
