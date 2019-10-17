package com.weiyan.example.apollo.demo.dynamic.es.client.configs;

import com.weiyan.example.apollo.demo.dynamic.es.client.annotation.DataSourceAnnotation;
import com.weiyan.example.apollo.demo.dynamic.es.client.client.DynamicRestClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/25 11:36
 */
@Component
public class CustomConfig {
    private final
    DynamicRestClient client;

    public CustomConfig(@Qualifier("client") DynamicRestClient client) {
        this.client = client;
    }


    @DataSourceAnnotation
    public Long customSource(String query,String url,@SuppressWarnings("unused") String dbId) {
        RestClient restClient = client.targetContainer();
        // 配置请求参数
        Map<String, String> paramMap = new HashMap<>(10);
        paramMap.put("pretty", "true");
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        Request request = new Request("GET", url);
        for (Map.Entry<String, String> stringStringEntry : paramMap.entrySet()) {
            request.addParameter(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        request.setEntity(entity);
        Response indexResponse;
        try {
            indexResponse = restClient.performRequest(request);
            return indexResponse.getEntity().getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }
}
