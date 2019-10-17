package com.weiyan.example.apollo.demo.dynamic.logbak.dynamic.logback.es.client.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建用于存储任意数据的路由容器
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/26 16:43
 */
@SuppressWarnings("unused")
public abstract class AbstractRoutingContainer<T> implements InitializingBean {
    /**
     * 预存储 缺省情况的数据内容
     */
    @Nullable
    private Object defaultTargetContainer;

    /**
     * 预存储 数据内容
     */
    @Nullable
    Map<Object, Object> targetContainers;

    /**
     * 存储内容 缺省情况的数据内容
     */
    @Nullable
    private T defaultResolvedContainer;

    /**
     * 存储内容 数据内容
     */
    @Nullable
    private HashMap<Object, T> resolvedContainers;

    /**
     * 设置 预存储 缺省情况的数据内容
     *
     * @param defaultTargetContainer 预存储 缺省情况的数据内容
     */
    public void setDefaultTargetContainer(Object defaultTargetContainer) {
        this.defaultTargetContainer = defaultTargetContainer;
    }

    /**
     * 设置 预存储 数据内容
     *
     * @param targetContainers 预存储 数据内容
     */
    public Map<Object, Object> setTargetContainers(Map<Object, Object> targetContainers) {
        Map<Object, Object> containers = this.targetContainers;
        this.targetContainers = targetContainers;
        return containers;
    }

    /**
     * 将与存储 数据 写入存储内容
     */
    @Override
    public void afterPropertiesSet() {
        if (this.defaultTargetContainer == null) {
            throw new IllegalArgumentException("Property 'defaultTargetContainer' is required");
        } else if (this.targetContainers == null) {
            throw new IllegalArgumentException("Property 'targetContainers' is required");
        } else {
            this.defaultResolvedContainer = this.resolveSpecifiedLookupValue(this.defaultTargetContainer);
            this.resolvedContainers = new HashMap<>(this.targetContainers.size());
            this.targetContainers.forEach((key, value) -> {
                Object lookupKey = this.resolveSpecifiedLookupKey(key);
                T lookupValue = this.resolveSpecifiedLookupValue(value);
                this.resolvedContainers.put(lookupKey, lookupValue);
            });
        }
    }

    /**
     * 获取key值
     *
     * @param lookupKey key的值
     * @return Object
     */
    private Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    /**
     * 获取value的值
     *
     * @param lookupValue value的值
     * @return T 返回的值
     */
    @SuppressWarnings("all")
    private T resolveSpecifiedLookupValue(Object lookupValue) throws IllegalArgumentException {
        try {
            return (T) lookupValue;
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal param and String supported: " + lookupValue);
        }
    }

    /**
     * 获取当前 Container router 的container
     *
     * @return T
     */
    public T targetContainer() {
        Assert.notNull(this.resolvedContainers, "Container router not initialized");
        Object lookupKey = this.determineCurrentLookupKey();
        T container = this.resolvedContainers.get(lookupKey);
        if (container == null) {
            container = this.defaultResolvedContainer;
        }
        if (container == null) {
            throw new IllegalStateException("Cannot determine target Container for lookup key [" + lookupKey + "]");
        } else {
            return container;
        }
    }

    /**
     * 设置当前Container 的路由
     *
     * @return Object
     */
    abstract Object determineCurrentLookupKey();

}
