package com.biykcode.dynamicdatasource.config;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariableDataSource {

    String dataSource() default "";
}
