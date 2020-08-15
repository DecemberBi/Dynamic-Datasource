package com.biykcode.dynamicdatasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
public class Config {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean("masterProperties")
    @ConfigurationProperties("datasource.master")
    public DataSourceProperties masterProperties() {
        return new DataSourceProperties();
    }

    @Bean("readonlyProperties")
    @ConfigurationProperties("datasource.readonly")
    public DataSourceProperties readonlyProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource1() {
        DataSourceProperties masterProperites = masterProperties();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(masterProperites.getUrl());
        dataSource.setUsername(masterProperites.getUsername());
        dataSource.setPassword(masterProperites.getPassword());
        dataSource.setDriverClassName(masterProperites.getDriverClass());
        return dataSource;
    }

    @Bean
    public DataSource dataSource2() {
        DataSourceProperties readonlyProperties = readonlyProperties();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(readonlyProperties.getUrl());
        dataSource.setUsername(readonlyProperties.getUsername());
        dataSource.setPassword(readonlyProperties.getPassword());
        dataSource.setDriverClassName(readonlyProperties.getDriverClass());
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
        bean.setMapperLocations(resolveMapperLocations(new String[]{mapperLocations}));
        bean.setDataSource(dynamicDataSource());
        bean.setConfiguration(configuration);
        return bean;
    }

}
