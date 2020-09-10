package com.ch.web;

import com.ch.exceptions.BusinessException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 *
 * 登录用户信息绑定器
 *
 * 将LoginUserBag的用户信息绑定的controller方法的标有@LoginUser注解的参数上
 *
 *
 */
public class CurrentUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object user = CurrentUserBag.get();
        if(null == user) {
            throw new BusinessException("未找到当前登录用户信息，请检查");
        }
        return user;
    }
}
