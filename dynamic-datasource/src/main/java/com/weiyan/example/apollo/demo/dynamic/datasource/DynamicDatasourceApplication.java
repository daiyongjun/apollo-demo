package com.weiyan.example.apollo.demo.dynamic.datasource;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 项目启动类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/23 21:13
 */
@EnableApolloConfig
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceApplication.class, args);
    }

}
