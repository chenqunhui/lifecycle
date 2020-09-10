//package com.ch.configurations;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.mapping.DatabaseIdProvider;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
//import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ResourceLoader;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.util.List;
//
//
//@Configuration
//@Slf4j
//public class DatasourceConfiguration extends DruidDataSourceAutoConfigure {
//
//
//
////    @Bean(name="dataSource", destroyMethod = "close")
////    public DruidDataSource createDataSource() throws IOException {
////    	log.info("====== DataSource init start ! =======");
////        DruidDataSource dataSource = new DruidDataSource();
////        dataSource.setDriverClassName(env.getProperty("jdbc.driver-class-name"));
////        dataSource.setUrl(env.getProperty("jdbc.url"));
////        dataSource.setUsername(env.getProperty("jdbc.username"));
////        dataSource.setPassword(env.getProperty("jdbc.password"));
////        dataSource.configFromPropety(PropUtils.load(DatasourceConfiguration.class,"druid.properties"));
////        log.info("====== DataSource init success ! =======");
////        return dataSource;
////    }
//
////    @Bean(name="sqlSessionFactory")
////    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource) throws Exception{
////        return this.getSqlSessionFactory(dataSource);
////    }
//}
