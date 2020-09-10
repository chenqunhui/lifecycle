package com.ch.log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceLogIdUtils {
    public static String TRACEID = "traceId";


    public static String  createTraceId(){
        return UUID.randomUUID().toString();
    }

    public static String getCurrentThreadTraceLogId() {
        return MDC.get(TraceLogIdUtils.TRACEID);
    }
}
