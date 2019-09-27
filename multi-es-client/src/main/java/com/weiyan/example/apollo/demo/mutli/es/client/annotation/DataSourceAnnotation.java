package com.weiyan.example.apollo.demo.mutli.es.client.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解切换数据源
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/24 14:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface DataSourceAnnotation {
}
