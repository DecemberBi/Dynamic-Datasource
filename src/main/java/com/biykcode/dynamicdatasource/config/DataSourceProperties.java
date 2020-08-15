package com.biykcode.dynamicdatasource.config;

import lombok.Data;

@Data
public class DataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClass;

}
