package com.ch.configurations;

import com.ch.web.CurrentUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;



@Configuration
public class SwaggerConfiguration {

    @ConditionalOnExpression("!'${spring.profile.active}'.equals('prod')")
    @Bean
    public Docket api() {
        List<Parameter> pars = new ArrayList<Parameter>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name("token").description("用户token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(CurrentUser.class)
                .groupName(System.getProperty("spring.application.name"))
                .select().paths(regex("/*.*"))
                .apis(RequestHandlerSelectors.basePackage("com.ch"))
                .build()
                .globalOperationParameters(pars);

    }
}
