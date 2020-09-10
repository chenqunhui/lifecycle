package com.ch.log;

import com.alibaba.fastjson.JSON;
import com.ch.utils.MethodUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * 方法级Log打印
 *
 */


@Slf4j
@Component
@Aspect
public class MethodLogAspect {

    @Pointcut("@annotation(com.ch.log.LogPrinter)")
    protected void pointcut(){

    }

    @Around("pointcut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        StringBuilder logString = new StringBuilder();
        try{
            Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
            Class<?> clazz = method.getDeclaringClass();
            String[] dirs = clazz.getName().split("\\.");
            String className  = dirs[dirs.length-1].split("\\$")[0];
            Map<String,Object> params = MethodUtils.transferParams(proceedingJoinPoint.getArgs());
            logString.append("Call ")
                    .append(className)
                    .append(".")
                    .append(method.getName())
                    .append(" with param [")
                    .append(JSON.toJSON(params))
                    .append("]");
        }catch (Throwable t){
        }
        try {
            Object obj = proceedingJoinPoint.proceed();
            logString.append(" success , result is[")
                    .append(JSON.toJSON(obj))
                    .append("]");
            log.info(logString.toString());
            return obj;
        } catch (Throwable t) {
            logString.append(" throw exception !");
            log.error(logString.toString(),t);
            throw t;
        }
    }
}
