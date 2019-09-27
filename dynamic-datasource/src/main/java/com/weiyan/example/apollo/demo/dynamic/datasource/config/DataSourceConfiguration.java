package com.weiyan.example.apollo.demo.dynamic.datasource.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.weiyan.example.apollo.demo.dynamic.datasource.ds.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * 数据源加载类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/25 16:15
 */
@Configuration
public class DataSourceConfiguration {

    public final static String DATASOURCE_TAG = "db";

    private final ApplicationContext context;

    @ApolloConfig
    private Config config;


    @Autowired
    public DataSourceConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @Bean("datasource")
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource source = new DynamicDataSource();
        source.setTargetDataSources(Collections.singletonMap(DATASOURCE_TAG, dataSource()));
        return source;
    }

    @ApolloConfigChangeListener(interestedKeyPrefixes = "spring.datasource.")
    @SuppressWarnings("unused")
    public void onChange(ConfigChangeEvent changeEvent) {
        DynamicDataSource source = context.getBean(DynamicDataSource.class);
        //消除现存的数据源实例
        source.setTargetDataSources(Collections.singletonMap(DATASOURCE_TAG, dataSource()));
        source.afterPropertiesSet();
        System.out.println("当前数据源地址:\t" + config.getProperty("spring.datasource.jdbc-url", ""));
    }

    /**
     * 构建数据源实例
     *
     * @return  DataSource
     */
    private DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(config.getProperty("spring.datasource.driver-class-name", ""));
        dataSource.setJdbcUrl(config.getProperty("spring.datasource.jdbc-url", ""));
        dataSource.setUsername(config.getProperty("spring.datasource.username", ""));
        dataSource.setPassword(config.getProperty("spring.datasource.password", ""));
        return dataSource;
    }
}



