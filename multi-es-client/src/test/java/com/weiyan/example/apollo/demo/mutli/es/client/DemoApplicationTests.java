package com.weiyan.example.apollo.demo.mutli.es.client;

import com.weiyan.example.apollo.demo.mutli.es.client.configs.CustomConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    CustomConfig config;

    @Test
    public void contextLoads() throws Exception {
        for (int i = 0; i < 10000000; i++) {
            String query = "{\"size\":0,\"query\": {\"match_all\": {}}}";
            String url = "/media_ifengvideo/_search";
//        config1,config2
            System.out.println("路由一:\t" + config.customSource(query, url, "config1"));
            url = "/weiboaccount_01/_search";
            System.out.println("路由二:\t" + config.customSource(query, url, "config2"));
        }
    }
}
