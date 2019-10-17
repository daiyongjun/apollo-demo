package com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client.configs;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client.client.DynamicRestClient;
import com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client.client.DynamicRestClientHolder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * es-client的链接配置类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/26 14:15
 */
@Configuration
public class EsearchClientConfig implements EnvironmentAware {

    private Environment environment;

    /**
     * 创建线程池
     */
    private ScheduledExecutorService executor;

    private final ApplicationContext context;

    @Autowired
    public EsearchClientConfig(ApplicationContext context) {
        this.context = context;
        //建议手动创建线程池，官方提供的FixedThreadPool和SingleThreadPool允许请求队列的长度为Integer.MAX_VALUE最大值
        //ScheduledExecutorService 默认使用的DelayedWorkQueue  private static final int INITIAL_CAPACITY = 16;
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("close-client-%d").build();
        this.executor = new ScheduledThreadPoolExecutor(1, threadFactory);
    }

    /**
     * 参数绑定工具 springboot2.0新推出
     */
    private Binder binder;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        binder = Binder.get(environment);
    }

    @Bean(name = "client", destroyMethod = "destroy")
    public DynamicRestClient restClient() {
        DynamicRestClient dynamicRestClient = new DynamicRestClient();
        LinkedHashMap<Object, Object> restClients = new LinkedHashMap<>();
        List<String> customDataSourceNames = new ArrayList<>();
        initRestClient(restClients, customDataSourceNames);
        dynamicRestClient.setDefaultTargetContainer(restClients.get(customDataSourceNames.get(0)));
        dynamicRestClient.setTargetContainers(restClients);
        dynamicRestClient.afterPropertiesSet();
        return dynamicRestClient;
    }

    /**
     * apollo刷新应用地址
     *
     * @param changeEvent 监听事件
     */
    @ApolloConfigChangeListener(interestedKeyPrefixes = "spring.custom.elasticsearch.")
    @SuppressWarnings("unused")
    public void onChange(ConfigChangeEvent changeEvent) {
        DynamicRestClient dynamicRestClient = context.getBean(DynamicRestClient.class);
        LinkedHashMap<Object, Object> restClients = new LinkedHashMap<>();
        this.context.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        List<String> customDataSourceNames = new ArrayList<>();
        initRestClient(restClients, customDataSourceNames);
        //消除现存的数据源实例
        dynamicRestClient.setDefaultTargetContainer(restClients.get(customDataSourceNames.get(0)));
        Map<Object, Object> containers = dynamicRestClient.setTargetContainers(restClients);
        dynamicRestClient.afterPropertiesSet();
        //匿名实例化
        executor.execute(() -> {
            try {
                //RestClient的超时时间为30秒为了防止最长的请求未结束,等待36000后关闭链接
                Thread.sleep(36000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dynamicRestClient.destroy(containers);

        });
    }

    private void initRestClient(LinkedHashMap<Object, Object> restClients, List<String> customDataSourceNames) {
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        String names = environment.getProperty("spring.custom.elasticsearch.names", "");
        String regex = ",";
        // 多个数据源
        assert names != null;
        DynamicRestClientHolder.datasourceId.clear();
        for (String name : names.split(regex)) {
            Map properties = binder.bind("spring.custom.elasticsearch." + name, Map.class).get();
            RestClient restClient = buildRestClient(properties);
            customDataSourceNames.add(name);
            DynamicRestClientHolder.datasourceId.add(name);
            restClients.put(name, restClient);
            System.out.println("Data source initialization 【" + name + "】 successfully ...");
        }
    }

    private RestClient buildRestClient(Map properties) {
        String username = properties.get("username").toString();
        String password = properties.get("password").toString();
        String hosts = properties.get("hosts").toString();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        return RestClient.builder(setHttpHosts(hosts))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)).build();
    }

    /**
     * 根据初始化hosts列表，配置httpHosts数组
     *
     * @return HttpHost[]
     */
    private HttpHost[] setHttpHosts(String host) {
        HttpHost[] httpHosts = null;
        if (host != null) {
            String[] hosts = host.split(",");
            int size = hosts.length;
            httpHosts = new HttpHost[size];
            for (int i = 0; i < size; i++) {
                String[] tmp = hosts[i].split(":");
                HttpHost newHttpHost = new HttpHost(tmp[0], Integer.valueOf(tmp[1]), "http");
                httpHosts[i] = newHttpHost;
            }
        }
        return httpHosts;
    }

}
