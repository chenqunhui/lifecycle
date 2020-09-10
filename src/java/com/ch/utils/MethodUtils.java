package com.ch.utils;

import java.util.Map;
import java.util.TreeMap;

public class MethodUtils {

    public static TreeMap<String,Object> transferParams(Object[] args){
        TreeMap<String,Object> params = new TreeMap<>();
        if(null != args && args.length>0){
            for(int index =0;index<args.length;index++){
                Object obj = args[index];
                params.put("arg"+index,obj);
            }
        }
        return  params;
    }
}
