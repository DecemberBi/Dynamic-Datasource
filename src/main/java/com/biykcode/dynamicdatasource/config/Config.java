package com.biykcode.dynamicdatasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.management.MemoryType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
public class Config {

    @Bean
    public DataSource dataSource1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public DataSource dataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", dataSource1());
        targetDataSources.put("readonly", dataSource2());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        return (Resource[]) Stream.of((Object[]) Optional.ofNullable(mapperLocations).orElse(new String[0])).flatMap((location) -> {
            return Stream.of(getResources((String) location));
        }).toArray((x$0) -> {
            return new Resource[x$0];
        });
    }

    PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException var3) {
            return new Resource[0];
        }
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {

        Environment environment = new Environment("dev", new JdbcTransactionFactory(), dynamicDataSource());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setEnvironment(environment);
        MybatisProperties mybatisProperties = new MybatisProperties();
        mybatisProperties.setConfiguration(configuration);
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setMapperLocations(resolveMapperLocations(new String[]{"classpath:mybatis/*.xml"}));
        bean.setDataSource(dynamicDataSource());
        bean.setConfiguration(configuration);
        return bean;
    }

    /*@Bean
    public SqlSessionFactory sqlSessionFactory() {
        Environment environment = new Environment("dev", new JdbcTransactionFactory(), dynamicDataSource());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setEnvironment(environment);
        configuration.addMappers("classpath:mybatis/*.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory build = sqlSessionFactoryBuilder.build(configuration);
        return build;
    }*/


}
