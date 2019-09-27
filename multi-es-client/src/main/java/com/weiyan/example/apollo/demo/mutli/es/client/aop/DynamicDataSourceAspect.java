package com.weiyan.example.apollo.demo.mutli.es.client.aop;

import com.weiyan.example.apollo.demo.mutli.es.client.annotation.DataSourceAnnotation;
import com.weiyan.example.apollo.demo.mutli.es.client.client.DynamicRestClientHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 针对自定义注解类进行aop处理
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/24 14:30
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {
    /**
     * 设置切换的数据源标识
     *
     * @param point                切入点
     * @param dataSourceAnnotation 监听的注解名称
     */
    @Before("@annotation(dataSourceAnnotation)")
    public void changeDataSource(JoinPoint point, DataSourceAnnotation dataSourceAnnotation) {
        Object[] methodArgs = point.getArgs();
        String dsId = methodArgs[methodArgs.length - 1].toString();

        if (!DynamicRestClientHolder.existDataSource(dsId)) {
            System.err.println("No data source found ...【" + dsId + "】");
        } else {
            DynamicRestClientHolder.setDataSourceType(dsId);
        }
    }

    /**
     * 销毁数据源切换标识
     *
     * @param dataSourceAnnotation 监听的注解名称
     */
    @After("@annotation(dataSourceAnnotation)")
    public void destroyDataSource(DataSourceAnnotation dataSourceAnnotation) {
        DynamicRestClientHolder.clearDataSourceType();
    }
}
