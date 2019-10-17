package com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client;

import com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client.configs.CustomConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    CustomConfig config;

    @Test
    public void contextLoads() throws Exception {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("task-pool-%d").build();




        //        int corePoolSize,
//        int maximumPoolSize,
//        long keepAliveTime,
//        TimeUnit unit,
//        BlockingQueue<Runnable> workQueue,
//        ThreadFactory threadFactory
        ExecutorService executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1024), threadFactory);
        for (int z = 0; z < 10; z++) {
            executor.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    String query = "{\"size\":0,\"aggs\":{\"time\":{\"date_histogram\":{\"field\":\"news_posttime\",\"interval\":\"week\",\"format\":\"yyyy-MM-dd\"},\"aggs\":{\"statatic\":{\"range\":{\"keyed\":true,\"field\":\"news_positive\",\"ranges\":[{\"key\":\"负面\",\"to\":0.45},{\"key\":\"正常\",\"from\":0.45,\"to\":0.75},{\"key\":\"消极\",\"from\":0.75}]}}}}}}";
                    String url = "weibo_201908,weibo_201909/_search?pretty=true";
//        config1,config2
                    System.out.println("路由一:\t" + config.customSource(query, url, "config1"));
                    String query1 = "{\"size\":0,\"query\": {\"match_all\": {}}}";
                    url = "/weiboaccount_01/_search";
                    System.out.println("路由二:\t" + config.customSource(query1, url, "config2"));
                }
            });
        }

        Thread.sleep(10000000);
    }
}
