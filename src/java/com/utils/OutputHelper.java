/** 
 * Project Name:advertise-delivery-core <br>
 * File Name:OutputHelper.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 *//*
package com.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

*//**
 * @author wayne
 * @date 2017年8月4日 下午1:43:10
 *//*
public final class OutputHelper {
	
	private static final Log logger = LogFactory.getLog(OutputHelper.class);
	
	private OutputHelper() {
		
	}

	*//**
	 * 以UTF-8的编码方式将指定内容以纯文本的方式输出到客户端
	 * 
	 * @param content
	 *//*
	public static void outputPlainText(String content, HttpServletResponse response) {
    	output(content, "text/plain;charset=UTF-8", response);
    }
	
	*//**
	 * 以UTF-8的编码方式将指定内容以application/json的方式输出到客户端
	 * 
	 * @param content
	 * @param response
	 *//*
	public static void outputJSON(String content, HttpServletResponse response) {
    	output(content, "application/json;charset=UTF-8", response);
    }
	
	public static void outputJavascript(String content, HttpServletResponse response) {
    	output(content, "application/javascript;charset=UTF-8", response);
    }
	
	*//**
	 * 以UTF-8的编码方式将指定内容以application/xml的方式输出到客户端
	 * 
	 * @param content
	 * @param response
	 *//*
	public static void outputXML(String content, HttpServletResponse response) {
    	output(content, "application/xml;charset=UTF-8", response);
    }
	
	*//**
	 * 
	 * @param content
	 * @param characterEncoding
	 * @param response
	 *//*
	public static void outputPlainText(String content, String characterEncoding, HttpServletResponse response) {
    	output(content, "text/plain", characterEncoding, response);
    }
	
	*//**
	 * 
	 * @param content
	 * @param characterEncoding
	 * @param response
	 *//*
	public static void outputJSON(String content, String characterEncoding, HttpServletResponse response) {
    	output(content, "application/json", characterEncoding, response);
    }
	
	*//**
	 * 
	 * @param content
	 * @param characterEncoding
	 * @param response
	 *//*
	public static void outputXML(String content, String characterEncoding, HttpServletResponse response) {
    	output(content, "application/xml", characterEncoding, response);
    }
	
	*//**
	 * 以UTF-8的编码方式将指定内容以HTML的方式输出到客户端
	 * 
	 * @param content
	 *//*
	public static void outputHTML(String content, HttpServletResponse response) {
    	output(content, "text/html;charset=UTF-8", response);
    }
    
	*//**
	 * 以指定的Content-Type输出指定的内容到客户端
	 * 
	 * @param content
	 * @param cntentType
	 *//*
	public static void output(String content, String contentType, HttpServletResponse response){
        response.setContentType(contentType);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter writer = null;
        try{
        	writer = response.getWriter();
        	writer.write(content);
        }catch(IOException e){
        	if(logger.isErrorEnabled()) {
        		logger.error("Output content[" + content + "] by ContentType[" + contentType + "] failed.", e);
        	}
        	
        }finally {
        	if(writer != null) {
        		writer.close();
        	}
        }
    }
	
	*//**
	 * 该方法允许分别指定contentType和characterEncoding
	 * 
	 * @param content
	 * @param contentType
	 * @param characterEncoding
	 * @param response
	 *//*
	public static void output(String content, String contentType, String characterEncoding, HttpServletResponse response){
        response.setContentType(contentType);
        response.setCharacterEncoding(characterEncoding);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter writer = null;
        try{
        	writer = response.getWriter();
        	writer.write(content);
        }catch(IOException e){
        	if(logger.isErrorEnabled()) {
        		logger.error("Output content[" + content + "] by ContentType[" + contentType + "] and CharacterEncoding[" + characterEncoding + "] failed.", e);
        	}
        }finally {
        	if(writer != null) {
        		writer.close();
        	}
        }
    }
}
*/