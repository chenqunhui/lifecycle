package com.ch.web;



import com.ch.constants.Constants;
import com.ch.log.TraceLogIdUtils;
import com.ch.model.UserInfo;
import com.ch.service.UserInfoService;
import com.ch.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * 将httpHeader里用户信息转换为通过注解绑定
 */

@Slf4j
public class CurrentUserFilter extends GenericFilterBean {

    private UserInfoService userInfoService;

    private PathMatcher pathMatcher = new AntPathMatcher();
    private CurrentUserFilterConfig currentUserFilterConfig;


    public CurrentUserFilter(UserInfoService userInfoService,CurrentUserFilterConfig currentUserFilterConfig){
        this.userInfoService = userInfoService;
        this.currentUserFilterConfig = currentUserFilterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        String path = RequestUtils.getNotNullPathInfo(req);
        if(isIgnorPath(path)){
            if(log.isDebugEnabled()){
                log.debug("current path '{}' is ignored in CurrentUserFilter");
            }
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String token = req.getHeader(Constants.HTTP_HEAD_TOKEN);
        if(StringUtils.isEmpty(token)){
            //前端在某些场景下，必须用url跳转的方式交互，此时不能设置header，只能通过get参数处理
            token = req.getParameter(Constants.HTTP_HEAD_TOKEN);
        }
        if(log.isDebugEnabled()){
            log.debug("current path '{}' token is {}",path,token);
        }
        if (StringUtils.isEmpty(token)) {
            writeLoginErrsg(resp,"用户未登录");
            return;
        }

        CurrentUser currentUser = userInfoService.getUserInfoByToken(token);
        if(null == currentUser){
            writeLoginErrsg(resp,"用户未登录");
            return;
        }else {
            CurrentUserBag.cleanAndSet(currentUser);
        }
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            CurrentUserBag.clean();
        }
    }

    private boolean isIgnorPath(String path){
        if(CollectionUtils.isEmpty(currentUserFilterConfig.getIgnorePaths())){
            return false;
        }
        for(String ignorePath : currentUserFilterConfig.getIgnorePaths()){
            if(pathMatcher.match(ignorePath, path)){
                return true;
            }
        }
        return false;
    }

    private void writePermissionErrsg(HttpServletResponse resp, String errMsg){
        resp.setHeader("Content-type","text/html;charset=UTF-8");
        resp.setStatus(HttpStatus.SC_FORBIDDEN);
        try {
            resp.getWriter().println(errMsg);
        } catch (IOException e) {
        }
    }

    private void writeLoginErrsg(HttpServletResponse resp,String errMsg){
        resp.setHeader("Content-type","text/html;charset=UTF-8");
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            resp.getWriter().println(errMsg);
        } catch (IOException e) {
        }
    }
}