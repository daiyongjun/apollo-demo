package com.weiyan.example.apollo.demo.dynamic.es.client.client;


import org.elasticsearch.client.RestClient;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;

/**
 * 构建自定义的DynamicRestClient
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/26 14:30
 */
public class DynamicRestClient extends AbstractRoutingContainer<RestClient> {


    @Override
    Object determineCurrentLookupKey() {
        return DynamicRestClientHolder.getDataSourceType();
    }


    /**
     * 对象销毁方法
     */
    @PreDestroy
    public void destroy() {
        Map<Object, Object> containers = this.targetContainers;
        assert containers != null;
        destroy(containers);
    }

    /**
     * 手动清除链接信息
     *
     * @param containers 链接
     */
    public void destroy(Map<Object, Object> containers) {
        containers.forEach((key, value) -> {
            RestClient restClient = (RestClient) value;
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
