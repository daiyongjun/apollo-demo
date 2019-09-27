package com.weiyan.example.apollo.demo.mutli.datasource.configer;

import com.weiyan.example.apollo.demo.mutli.datasource.custom.DynamicDataSource;
import com.weiyan.example.apollo.demo.mutli.datasource.custom.DynamicDataSourceContextHolder;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化自定义的datasource
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/23 21:13
 */
@Configuration
public class CustomDataSourceConfig implements EnvironmentAware {

    private List<String> customDataSourceNames = new ArrayList<>();

    private Environment environment;

    /**
     * 参数绑定工具 springboot2.0新推出
     */
    private Binder binder;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        binder = Binder.get(environment);
    }

    /**
     * 初始化 DynamicDataSource multi-datasouce
     *
     * @return AbstractRoutingDataSource
     */
    @Bean(name = "custom")
    public AbstractRoutingDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        LinkedHashMap<Object, Object> dataSources = new LinkedHashMap<>();
        initCustomDataSources(dataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSources.get(customDataSourceNames.get(0)));
        dynamicDataSource.setTargetDataSources(dataSources);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

    /**
     * 详细初始化数据源
     *
     * @param dataSources 按顺序存储的数据源容器
     */
    private void initCustomDataSources(LinkedHashMap<Object, Object> dataSources) {
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        String names = environment.getProperty("spring.custom.datasource.names");
        String regex = ",";
        // 多个数据源
        assert names != null;
        for (String name : names.split(regex)) {
            Map properties = binder.bind("spring.custom.datasource." + name, Map.class).get();
            DataSource ds = buildDataSource(properties);
            if (null != ds) {
                customDataSourceNames.add(name);
                DynamicDataSourceContextHolder.datasourceId.add(name);
                dataSources.put(name, ds);
            }
            System.out.println("Data source initialization 【" + name + "】 successfully ...");
        }
    }

    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map properties) {
        Object type = properties.get("type");
        if (type == null) {
            type = "com.zaxxer.hikari.HikariDataSource";
        }
        Class<? extends DataSource> dataSourceType;
        try {
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = properties.get("driver-class-name").toString();
            String url = properties.get("jdbc-url").toString();
            String username = properties.get("username").toString();
            String password = properties.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            return factory.build();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}