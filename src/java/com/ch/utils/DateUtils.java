/** 
 * Project Name:advertise-common <br>
 * File Name:DateUtils.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

/**
 * 
 * @date 2017年8月4日 上午11:16:05
 */
public final class DateUtils {
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String DATE_FORMAT_YYYYMMDD="yyyyMMdd";
    
    private static ThreadLocal<Map<String,SimpleDateFormat>> dfMapThreadLocal = new ThreadLocal<Map<String, SimpleDateFormat>>() {  
        @Override  
        protected Map<String, SimpleDateFormat> initialValue() {  
            return new HashMap<String, SimpleDateFormat>();  
        }  
    };  

    private static SimpleDateFormat getSdf(final String pattern){
    	if(StringUtils.isEmpty(pattern)){
    		return null;
    	}
    	Map<String, SimpleDateFormat> sdfMap = dfMapThreadLocal.get();
    	SimpleDateFormat sdf = sdfMap.get(pattern);
    	if(sdf == null){
    		sdf = new SimpleDateFormat(pattern);
    		sdfMap.put(pattern, sdf);
    	}
    	return sdf;
    }
    
    /**
     * format date
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern){
    	if(date == null || StringUtils.isEmpty(pattern)){
    		return "";
    	}
    	return getSdf(pattern).format(date);
    }
    
    /**
     * parse dateStr
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String pattern) throws ParseException{
    	if(StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(pattern)){
    		return null;
    	}
    	return getSdf(pattern).parse(dateStr);
    }
    
    public static List<String> calEveryDay(Date startTime, Date endTime){
    	if(startTime == null || endTime == null || startTime.after(endTime)){
    		return Collections.emptyList();
    	}
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startTime);
    	List<String> dateList = new ArrayList<String>();
    	while(!cal.getTime().after(endTime)){
    		dateList.add(DateUtils.format(cal.getTime(), "yyyy-MM-dd"));
    		cal.add(Calendar.DAY_OF_YEAR, 1);
    	}
    	return dateList;
    }
		
	public static Date startOfDay(Date date) {
        return new DateTime(date).dayOfYear().roundFloorCopy().toDate();
    }
		
	public static Date endOfDay(Date date) {
        DateTime startDateTime = new DateTime(date).dayOfYear().roundFloorCopy();
        return startDateTime.plusDays(1).minusMillis(1).toDate();
    }

	public static String getyyyyMMdd(Date  dt){
		return new SimpleDateFormat("yyyyMMdd").format(dt);
	}
	
	public static Date addHour(Date date,int hour){
		if (null == date) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date); // 设置当前日期
		c.add(Calendar.HOUR_OF_DAY, hour); 
		date = c.getTime();
		return date;
	}
	
	public static Date addDays(Date date,int days){
		if (null == date) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date); // 设置当前日期
		c.add(Calendar.DATE, days); // 日期加days天
		date = c.getTime();
		return date;
	}


	public static Date addMinutes(Date date,int minutes){
		if (null == date) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date); // 设置当前日期
		c.add(Calendar.MINUTE, minutes); //
		date = c.getTime();
		return date;
	}
	
	public static void main(String[] args){
			System.out.println(startOfDay(new Date()));
	}
}
