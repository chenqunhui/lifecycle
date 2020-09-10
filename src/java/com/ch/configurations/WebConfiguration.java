package com.ch.configurations;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ch.service.UserInfoService;
import com.ch.web.CurrentUserFilter;
import com.ch.web.CurrentUserFilterConfig;
import com.ch.web.CurrentUserResolver;
import com.ch.web.HttpInterceptor;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * spring mvc JSON处理
 *
 */

@Configuration
public class WebConfiguration {

//    @Bean
//    public HttpMessageConverters gsonHttpMessageConverters() {
//        //需要定义一个convert转换消息的对象;
//        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
//        GsonBuilder gb = new GsonBuilder();
//
//        //日期格式
//        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
//        gsonConverter.setGson(gb.create());
//
//        //处理中文乱码问题
//        gsonConverter.setDefaultCharset(Charset.forName("UTF-8"));
//        //支持类型
//        List<MediaType> mediaTypes = new ArrayList<>();
//        mediaTypes.add(MediaType.TEXT_HTML);
//        mediaTypes.add(MediaType.APPLICATION_JSON);
//        gsonConverter.setSupportedMediaTypes(mediaTypes);
//        gsonConverter.setSupportedMediaTypes(mediaTypes);
//        return new HttpMessageConverters(gsonConverter);
//    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.TEXT_HTML);
        //在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(converter);
    }

    @Bean
    public CurrentUserFilterConfig getCurrentUserFilterConfig(){
        return new CurrentUserFilterConfig();
    }

    @Bean
    public FilterRegistrationBean filterRegistration(CurrentUserFilterConfig currentUserFilterConfig, UserInfoService userInfoService) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CurrentUserFilter(userInfoService,currentUserFilterConfig));
        registration.addUrlPatterns("/*");
        registration.setName("currentUserFilter");
        registration.setOrder(1);
        return registration;
    }



    /**
     * CurrentUser参数绑定器
     * @return
     */
    @Bean
    public  WebMvcConfigurer springMvcResolver(HttpInterceptor httpInterceptor){
        return new WebMvcConfigurer(){
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(new CurrentUserResolver());

            }
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //增加http拦截器
                registry.addInterceptor(httpInterceptor).addPathPatterns("/**");
            }
        };


    }

}
