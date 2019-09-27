package com.weiyan.example.apollo.demo.mutli.es.client;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/23 21:13
 */
@SpringBootApplication
@EnableApolloConfig

public class MultiEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiEsApplication.class, args);
    }
}
