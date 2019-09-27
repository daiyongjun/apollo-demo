package com.weiyan.example.apollo.demo.mutli.datasource;

import com.alibaba.fastjson.JSON;
import com.weiyan.example.apollo.demo.mutli.datasource.configer.CustomConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

/**
 * 运行测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/9/23 21:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    @Qualifier("other")
    private DataSource other;

    @Autowired
    @Qualifier("datasouce")
    private DataSource datasouce;

    @Autowired
    CustomConfig customConfig;

    @Test
    public void contextLoads() {
        // 查看配置数据源信息
        System.out.println(datasouce);
        System.out.println(datasouce.getClass().getName());
        System.out.println(dataSourceProperties);
        //执行SQL,输出查到的数据
        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasouce);
        List<?> resultList = jdbcTemplate.queryForList("select * from test");
        System.out.println("===>>>>>>>>>>>" + JSON.toJSONString(resultList));
    }

    @Test
    public void contextLoads1() {
        // 查看配置数据源信息
        System.out.println(other);
        System.out.println(other.getClass().getName());
        System.out.println(dataSourceProperties);
        //执行SQL,输出查到的数据
        JdbcTemplate jdbcTemplate = new JdbcTemplate(other);
        List<?> resultList = jdbcTemplate.queryForList("select * from test");
        System.out.println("===>>>>>>>>>>>" + JSON.toJSONString(resultList));
    }

    @Test
    public void customContextLoads() {
        String result = customConfig.customSource("db2");
        System.out.println("===>>>>>>>>>>>" + result);
    }
}
