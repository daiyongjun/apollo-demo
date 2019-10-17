package com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.datasource.custom;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 构建DataSource容器
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/24 14:16
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 获取数据源标识,用于区分获取当前什么数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
