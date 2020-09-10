package com.ch.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String getNotNullPathInfo(HttpServletRequest request){
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            uri = uri.substring(contextPath.length());
        }
        return uri;
    }
}
