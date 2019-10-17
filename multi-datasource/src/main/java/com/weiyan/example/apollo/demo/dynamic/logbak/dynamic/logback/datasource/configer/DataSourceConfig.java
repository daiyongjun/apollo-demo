package com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.datasource.configer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 定义其他的datasource
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/23 21:13
 */
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "datasouce")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "other")
    @ConfigurationProperties(prefix = "spring.datasource.other")
    public DataSource getMyDataSource() {
        return DataSourceBuilder.create().build();
    }
}