package com.ch.web;

import com.ch.exceptions.BusinessException;
import com.ch.log.TraceLogIdUtils;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@ControllerAdvice(annotations = {RestController.class,Controller.class})
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
	
	private static final Map<String,String> ignorTypes = new HashMap<String,String>();
	private static final List<String> ignoreBasePackage = new ArrayList<>();
	static{
		ignorTypes.put(GlobalResponse.class.getName(), "1");
		ignorTypes.put(ResponseEntity.class.getName(), "1");
	}


    /**
     * 排除不需要处理的类型
     * @param clazz
     */
	public static void addIgnoreHandleType(Class<?> clazz){
        ignorTypes.put(clazz.getName(), "1");
    }

    public static void addIgnoreBasePackage(String basePackage){
        ignoreBasePackage.add(basePackage);
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if(ignorTypes.containsKey(returnType.getParameterType().getName())){
            return false;
        }
        if(!ignoreBasePackage.isEmpty()){
            String methodClassName = returnType.getMethod().getDeclaringClass().getName();
            for(String basePackage : ignoreBasePackage){
                if(methodClassName.startsWith(basePackage)){
                    return false;
                }
            }
        }
        return true;
    }

	@Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType
            , MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType
            , ServerHttpRequest request, ServerHttpResponse response) {
        final String returnTypeName = returnType.getParameterType().getName();
        if ("void".equals(returnTypeName)) {
            return GlobalResponse.success(null);
        }
        return GlobalResponse.success(body);
    }
    



    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BusinessException.class})
    public <T> GlobalResponse<T> handleException(BusinessException e) {
        log.error(Throwables.getStackTraceAsString(e));
        return GlobalResponse.businessFail(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public <T> GlobalResponse<T> handleException(MethodArgumentNotValidException e) {
        log.error(Throwables.getStackTraceAsString(e));
        BindingResult bindingResult = e.getBindingResult();
        return GlobalResponse.exception(e, bindingResult.getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Throwable.class})
    public <T> GlobalResponse<T> handleThrowable(Throwable e) {
        log.error(Throwables.getStackTraceAsString(e));
        return GlobalResponse.exception(e,null);
    }
}
