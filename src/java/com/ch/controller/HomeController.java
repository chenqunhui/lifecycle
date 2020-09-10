package com.ch.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(description="测试",value="value",produces="produces",consumes = "consumes")
@RestController
public class HomeController {

	@GetMapping("/getHome")
	public String home() {
		return "SUCCESS";
	}

	@GetMapping("/")
	public String info(){
		return "test";
	}
	
}
