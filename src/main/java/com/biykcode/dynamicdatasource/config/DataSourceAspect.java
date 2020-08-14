package com.biykcode.dynamicdatasource.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(variableDataSource)")
    public void before(JoinPoint joinPoint, VariableDataSource variableDataSource) {
        String dataSource = variableDataSource.dataSource();
        DynamicDataSouceHolder.setDataSource(dataSource);
    }

    @After("@annotation(variableDataSource)")
    public void after(JoinPoint joinPoint, VariableDataSource variableDataSource) {
        DynamicDataSouceHolder.clearDataSource();
    }


}
