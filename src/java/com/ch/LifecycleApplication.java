package com.ch;

import com.sun.glass.ui.Application;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.FrameworkServlet;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.fasterxml.jackson.databind.ObjectMapper;


@MapperScan(basePackages={"com.ch.dao"})
@EnableSwagger2
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.ch","org.mybatis.spring.boot.autoconfigure"})
@SpringBootApplication
public class LifecycleApplication {

    private FrameworkServlet servlet;
    public static void main(String[] args){
        new SpringApplicationBuilder().sources(LifecycleApplication.class).run(args);
    }
}
