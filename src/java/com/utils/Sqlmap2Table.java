package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;



public class Sqlmap2Table {

	 // 默认所有的varchar都是512，可以保证满足绝大多数的字段  
    private static final String DEFAULT_VARCHAR_LENGTH = "VARCHAR(256)";  
    
    
    static Map<String /*filename*/,Set<String> /*content*/> map = new ConcurrentHashMap<String,Set<String>>();
    
    static  final Set<String> notColumn = new HashSet<String>();
    static {
    	notColumn.add("*");
    }
    
  
    public static void main(String[] args) throws JDOMException, IOException {  
    	
        String sqlMapPath = "F:/workspace/coupon/coupon-service/src/main/resources/mybatis/sqlmap";
        
        //"F:/workspace5/config-server/src/main/resources/mybatis/mapper";//这里指定你的sqlmap配置文件所在路径  
        
        analysis(sqlMapPath); 
        
        //String str =   "CREATE TABLE `NewTable` (";
             //`id`  bigint NOT NULL ,
             //`name`  varchar(255) NULL ,
          //PRIMARY KEY (`id`))

        
        
        
        for(Entry<String,Set<String>> e :map.entrySet()){
        	StringBuilder sql = new StringBuilder("drop table if EXISTS `").append(e.getKey()).append("`;\r\n");
        	sql.append("CREATE TABLE `").append(e.getKey()).append("` (").append("\r\n");
        	sql.append("`id`  bigint NOT NULL ,\r\n");
        	for(String col : e.getValue()){
        		if("id".equals(col)){
        			continue;
        		}
        		if(col.startsWith("`")){
        			continue;
        		}
        		sql.append("`").append(col).append("`  varchar(255) NULL ,\r\n");
        	}
        	sql.append("PRIMARY KEY (`id`));");
        	System.out.println(sql);
        }
        
        
        
        
    }  
  
    /** 
     * 根据指定的目录进行遍历分析 
     *  
     * @param path 
     * @throws IOException 
     * @throws JDOMException 
     */  
    private static void analysis(String path) throws IOException, JDOMException {  
        File filePath = new File(path);  
        if (filePath.isDirectory() && !filePath.getName().equals(".svn") && !filePath.getName().equals(".git")) {  
            File[] fileList = filePath.listFiles();  
            for (File file : fileList) {  
                if (file.isDirectory()) {  
                    analysis(file.getAbsolutePath());  
                } else {  
                	analysisSqlmapFile(file.getAbsolutePath());  
                }  
            }  
        }  
    }  
  
    /** 
     * 分析单个的sqlmap配置文件 
     *  
     * @param sqlMapFile 
     * @throws IOException 
     * @throws JDOMException 
     */  
    private static void analysisSqlmapFile(String sqlMapFile) throws IOException, JDOMException { 
        /** 
         * 这里要把sqlmap文件中的这一行去掉：<br> 
         * <!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd"><br> 
         * 否则JDom根据文件创建Document对象时，会报找不到www.ibatis.com这个异常，导致渲染不成功。 
         */  
        String xmlString = filterRead(sqlMapFile, "<!DOCTYPE");  
        Document doc = getDocument(xmlString);  
        Element root = doc.getRootElement();
        List<Element> selectList= root.getChildren("select");
        for(Element e :selectList){
        	getTableNameAndColumn(e);
        }
        
        
          

    }  
    
    
    public static void getTableNameAndColumn(Element e){
    	String content = e.getValue().toLowerCase().trim();
    	if(content.trim().startsWith("select")){
    		transforSelect(content);
    	}
    }
    
    public static void transforSelect(String selectSql){
    	String[] strs = selectSql.split("from");
    	String[] columns = strs[0].trim().substring(6).trim().split(",");
    	String tabname = "";
    	try {
    		tabname = strs[1].trim().split(" ")[0].trim();
    	}catch(Exception e){
    		System.out.println(selectSql);
    		transforSelect(selectSql);
    		throw e;
    	}
    	Set<String> cols = map.get(tabname);
    	if(null == cols){
    		cols = new HashSet<String>();
    		cols.add("id");
    		map.put(tabname, cols);
    	}
    	for(String col : columns){
    		String s = col.split(" ")[0].trim();
    		if(notColumn.contains(s) || s.startsWith("count(") || s.startsWith("sum{") ){
    			continue;
    		}
    		if(s.contains(".")){
    			String[]   a = s.split("\\.");
    			s = a[1].trim();
    		}
    		cols.add(s);
    	}
    }
    
    /** 
     * 过滤性阅读 
     *  
     * @param filePath 文件路径 
     * @param notIncludeLineStartWith 不包括的字符，即某行的开头是这样的字符串，则在读取的时候该行忽略 
     * @return 
     * @throws IOException 
     */  
    private static String filterRead(String filePath, String notIncludeLineStartWith) throws IOException {  
        StringBuilder  sb = new StringBuilder();  
        FileReader fr = new FileReader(filePath);  
        BufferedReader br = new BufferedReader(fr);  
        String line ;
        boolean currentlineIsEnd = true;
        while ((line = br.readLine()) != null) {  
            if(line.startsWith("<?xml") || line.startsWith("<!DOCTYPE")){
            	if(!line.contains(">")){
            		currentlineIsEnd = false;
            	}
            	continue;
            }
            if(!currentlineIsEnd){
            	if(line.contains(">")){
            		currentlineIsEnd = true;
            	}
            	continue;
            }else{
            	sb.append(line);
            }
        }  
        br.close();  
        fr.close();  
        return sb.toString();  
    }  
  
    /** 
     * 根据XML 字符串 建立JDom的Document对象 
     *  
     * @param xmlString XML格式的字符串 
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。 
     * @throws IOException 
     * @throws JDOMException 
     */  
    private static Document getDocument(String xmlString) throws JDOMException, IOException {  
  
        SAXBuilder builder = new SAXBuilder();  
        Document anotherDocument = builder.build(new StringReader(xmlString));  
        return anotherDocument;  
  
    }  
}  