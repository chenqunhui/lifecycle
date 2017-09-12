package com.web;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;

import org.unidal.web.MVC;

@Controller
public class HomeController {

	@RequestMapping("/index.do")
	@ResponseBody
	public String home() {
		
		
		for (int i = 0; i < 100; i++) {
			Transaction t = Cat.getProducer().newTransaction("testHome", "testCatClient");
			try {
				//RPC
				throw new RuntimeException();
			} catch (Exception e) {
				Cat.getProducer().logError(e);// 用log4j记录系统异常，以便在Logview中看到此信息
				t.setStatus(e);
			} finally {
				try {
					Thread.sleep(new Double(Math.random()).longValue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t.complete();
			}
		
		}
		throw new RuntimeException();
		//return "WELCOME:OK!";
	}
	
}
