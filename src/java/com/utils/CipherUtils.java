/** 
 * Project Name:advertise-common <br>
 * File Name:CipherUtils.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES对称加密解密工具类
 * @date 2017年7月13日 下午1:59:36
 */
public class CipherUtils {

	private static final String ALGORITHM = "AES";
	private static SecretKeySpec key;
	private static volatile boolean init = false;
	private static String password = "dddddddddddddddddddddddddddd5";
	public static String encrypt(String content) {
		String result = null;
		try {
			if(!init){
				init();
			}
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			result = encrypt(cipher, key, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String encrypt(Cipher cipher, SecretKeySpec key, String content){
		String result = null;
		try {
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] resultBytes = cipher.doFinal(byteContent);
			result = Base64.encodeBase64String(resultBytes);
			result = result.replaceAll("\\+", "_")
						   .replaceAll("/", "-")
						   .replaceAll("=", ".")
						   .replaceAll("\\s", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String decrypt(String content){
		String result = null;
		try {
			if(!init){
				init();
			}
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			result = decrypt(cipher, key, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String decrypt(Cipher cipher, SecretKeySpec key, String content){
		String result = null;
		try {
			content = content.replaceAll("_", "+")
					         .replaceAll("-", "/")
					         .replaceAll("\\.", "=");
			byte[] encryptedData =  Base64.decodeBase64(content);
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] resultBytes = cipher.doFinal(encryptedData);
			result = new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static synchronized void init() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException{
		if(init){
			return;
		}
		KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
        secureRandom.setSeed(password.getBytes("UTF-8"));
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		key = new SecretKeySpec(enCodeFormat, ALGORITHM);
		init = true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
