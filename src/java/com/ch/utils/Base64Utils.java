/** 
 * Project Name:advertise-common <br>
 * File Name:Base64Utils.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * @date 2017年7月11日 下午3:56:43
 */
public class Base64Utils {

	public static String decode(String s){
		return new String(Base64.decodeBase64(s.getBytes()));
	}

	public static String encode(String s){
		return new String(Base64.encodeBase64(s.getBytes()));
	}
	
	public static void main(String[] args){
		System.out.println(decode("Lz9udWxs"));
	}
}
