package com.ch.utils;

public class StringUtils {

	public static boolean isEmpty(String str){

        return null == str || "".equals(str.trim());
	}


	/**
	 * 按最大尺寸切割前半段
	 * @param source
	 * @param maxLength
	 * @return
	 */
	public static String cutMaxLengthString(String source,int maxLength){
		if(isEmpty(source) || source.length()< maxLength){
			return source;
		}else {
			StringBuilder sb = new StringBuilder(source);
			return sb.subSequence(0,maxLength-1).toString();
		}
	}
}
