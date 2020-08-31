package com.ch.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {

	@RequestMapping("/index.do")
	@ResponseBody
	public String home() {
		
		
		for (int i = 0; i < 100; i++) {
			try {
				//RPC
				throw new RuntimeException();
			} catch (Exception e) {
			} finally {
				try {
					Thread.sleep(new Double(Math.random()).longValue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		throw new RuntimeException();
		//return "WELCOME:OK!";
	}
	
}
