package com.biykcode.dynamicdatasource.service;

import com.biykcode.dynamicdatasource.config.VariableDataSource;
import com.biykcode.dynamicdatasource.dao.DataSourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceDao dataSourceDao;

    @VariableDataSource(dataSource = "master")
    public int master() {
        int count = dataSourceDao.count();
        System.out.println(count);
        return count;
    }

    @VariableDataSource(dataSource = "readonly")
    public int readonly() {
        int count = dataSourceDao.count();
        System.out.println(count);
        return count;
    }
}
