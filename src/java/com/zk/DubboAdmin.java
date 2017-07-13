package com.zk;

import java.net.URLDecoder;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;


public class DubboAdmin {
    private static final String ZK_ADDRESS = "192.168.62.220:2182";
    
    private static String serverName = "CouponService";
    
    private static CuratorFramework client = ZkClientFactory.getInstance(ZK_ADDRESS);

    public static void main(String[] args) throws Exception {
    	
    	
    	List<String> services =  client.getChildren().forPath("/dubbo");
    	if(null == services || services.isEmpty()){
    		System.out.println("No service in Zk...");
    		System.exit(1);
    	}
    	System.out.println("Search Service ["+serverName+"] start...");
    	for(String str : services){
    		if(str.contains(serverName)){
    			printDetail(str);
    		}
    	}
    	System.out.println("Search Service ["+serverName+"] end." );
    }
    
    private static void printDetail(String str) throws Exception{
    	System.out.println("==================================");
    	System.out.println("CurrentNode is:"+ str);
    	List<String> children = client.getChildren().forPath("/dubbo/"+str);
    	for(String child :children){
    		List<String> nodes = client.getChildren().forPath("/dubbo/"+str+"/"+child);
    		System.out.println("  NodeName:"+child);
    		for(String node :nodes){
    			System.out.println("   "+URLDecoder.decode(node));
    		}
    	}
    }
    
    
    
    
    

        
        
      


}