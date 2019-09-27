package com.weiyan.example.apollo.demo.mutli.datasource.configer;

import com.alibaba.fastjson.JSON;
import com.weiyan.example.apollo.demo.mutli.datasource.annotation.DataSourceAnnotation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/25 11:36
 */
@Component
public class CustomConfig {
    private final DataSource custom;

    public CustomConfig(@Qualifier("custom") DataSource custom) {
        this.custom = custom;
    }

    @DataSourceAnnotation
    public String customSource(@SuppressWarnings("unused") String dbId) {
        System.out.println(custom);
        System.out.println(custom.getClass().getName());
        //执行SQL,输出查到的数据
        JdbcTemplate jdbcTemplate = new JdbcTemplate(custom);
        List<?> resultList = jdbcTemplate.queryForList("select * from test");
        return JSON.toJSONString(resultList);
    }
}
