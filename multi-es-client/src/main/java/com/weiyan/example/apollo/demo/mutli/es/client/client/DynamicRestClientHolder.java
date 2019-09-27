package com.weiyan.example.apollo.demo.mutli.es.client.client;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建multi-datasource记录容器,以及线程为副本的threadLocal作为切换标识
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/24 14:19
 */
public class DynamicRestClientHolder {

    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 管理所有的数据源id,用于数据源的判断
     */
    public static List<String> datasourceId = new ArrayList<>();

    /**
     * 设置访问当前数据源标识
     *
     * @param dataSourceType 设置当前访问数据源标识
     */
    public static void setDataSourceType(String dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 设置获取当前数据源标识
     *
     * @return String
     */
    static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空所有的数据源变量
     */
    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 按顺序存储当前数据源实例中存储的的数据源
     *
     * @param dataSourceType 数据源标识
     * @return boolean
     */
    public static boolean existDataSource(String dataSourceType) {
        return datasourceId.contains(dataSourceType);
    }
}
