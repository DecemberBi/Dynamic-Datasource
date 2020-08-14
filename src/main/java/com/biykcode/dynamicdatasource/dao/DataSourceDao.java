package com.biykcode.dynamicdatasource.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataSourceDao {

    int count();
}
