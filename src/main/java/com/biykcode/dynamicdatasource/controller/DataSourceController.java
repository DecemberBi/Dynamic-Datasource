package com.biykcode.dynamicdatasource.controller;

import com.biykcode.dynamicdatasource.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping("/master")
    public int master() {
        return dataSourceService.master();
    }

    @RequestMapping("/readonly")
    public int readonly() {
        return dataSourceService.readonly();
    }


}
