package com.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @date 2017年7月11日 上午10:25:56
 */
public class MessageDigester {
	
	/**
	 * 取得一个MD5摘要计算实例
	 * @return
	 */
	private static MessageDigest getDigester(ALG alg){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(alg.value());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md;
	}
	
	/**
     * @param s
     * @return
     */
    public static String SHA1Digest(String s)
    {
        byte [] bytes = digest(s, ALG.SHA1);
        return getHexString(bytes);
    }
    
    /**
     * @param s
     * @return
     */
    public static String SHA1Digest(String s, String encode)
    {
        byte [] result = digest(s, encode, ALG.SHA1);
        return getHexString(result);
    }
	
	/**
	 * @param s
	 * @return
	 */
	public static String Md5Digest(String s)
	{
	    byte [] bytes = digest(s, ALG.MD5);
		return getHexString(bytes);
	}
	
	/**
	 * @param s
	 * @return
	 */
	public static String Md5Digest(String s, String encode)
	{
		byte [] result = digest(s, encode, ALG.MD5);
		
		return getHexString(result);
	}
	
	/**
	 * @param input
	 * @return
	 */
	public static String Md5Digest(InputStream input)
	{
		MessageDigest md = getDigester(ALG.MD5);
		if(md==null) return null;
		byte [] result = null;
		byte[] buf = new byte[1024*5];
		int readCount = 0;
		try
		{
			while((readCount = input.read(buf))>0)
			{
				md.update(buf, 0 , readCount);
			}
			result = md.digest();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return getHexString(result);
	}
	
	private static byte [] digest(String s, ALG alg){
        MessageDigest md = getDigester(alg);
        if(md==null) return null;
        byte [] result = null;
        md.update(s.getBytes());
        result = md.digest();
        return result;
    }
	
	private static byte [] digest(String s, String encode, ALG alg){
        MessageDigest md = getDigester(alg);
        if(md==null) return null;
        byte [] result = null;
        try {
            md.update(s.getBytes(encode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = md.digest();
        return result;
    }
	
	/**
	 * @param result
	 * @return
	 */
	private static String getHexString(byte[] result)
	{
		StringBuffer sb = new StringBuffer();
		for(byte b : result)
		{
			String temp = Integer.toHexString(b&0xff);
			sb.append(temp.length()<2?"0"+temp:temp);
		}
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Md5Digest("123456admin"));
	}

	static enum ALG{
	    MD5, SHA1;
	    
	    public String value(){
	        return toString();
	    }
	}
}
