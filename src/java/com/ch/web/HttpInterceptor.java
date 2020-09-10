package com.ch.web;


import com.ch.constants.Constants;
import com.ch.log.OperateLogReporter;
import com.ch.log.TraceLogIdUtils;
import com.ch.model.OperateLog;
import com.ch.utils.IpUtils;
import com.ch.utils.RequestUtils;
import com.ch.utils.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
@Getter
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    @Qualifier("defaultOperateLogReporter")
    @Autowired(required = false)
    private OperateLogReporter reporter;

    private static ThreadLocal<OperateLog> cacheOperateLog = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String traceId = req.getHeader(Constants.HTTP_HEAD_TRACE_ID);
        if(null == traceId){
            traceId = req.getHeader(TraceLogIdUtils.TRACEID);
            if(null == traceId){
                traceId = TraceLogIdUtils.createTraceId();
            }
        }
        MDC.put(TraceLogIdUtils.TRACEID,traceId);

        RemoteInfo remoteInfo = RemoteInfo.builder()
                .imei(req.getHeader(Constants.HTTP_HEAD_IMEI))
                .ip(IpUtils.getRemoteIpAddr(req))
                .mac(req.getHeader(Constants.HTTP_HEAD_MAC))
                .token(req.getHeader(Constants.HTTP_HEAD_TOKEN))
                .traceId(traceId)
                .build();
        OperateLog operateLog = null;
        if(o instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod)o;
            operateLog = OperateLog.builder()
                    .traceId(traceId)
                    .createdTime(new Date())
                    .path(StringUtils.cutMaxLengthString(RequestUtils.getNotNullPathInfo(req),500))
                    .remoteInfo(remoteInfo)
                    .input(StringUtils.cutMaxLengthString("",5000))
                    .build();
        }else if (o instanceof ResourceHttpRequestHandler){

        }
        cacheOperateLog.set(operateLog);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try{
            OperateLog operateLog = cacheOperateLog.get();
            if(null != reporter && null != operateLog){
                operateLog.setOutput(StringUtils.cutMaxLengthString("",5000));
                reporter.report(operateLog);
            }
        }catch (Exception e){
            log.error("report operateLog error",e);
        } finally {
            MDC.clear();
            cacheOperateLog.remove();
        }


    }
}
