package com.weiyan.example.apollo.demo.dynamic.datasource;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDatasourceApplicationTests {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    @Qualifier("datasource")
    private DataSource datasouce;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            // 查看配置数据源信息
            System.out.println(datasouce);
            System.out.println(datasouce.getClass().getName());
            System.out.println(dataSourceProperties);
            //执行SQL,输出查到的数据
            JdbcTemplate jdbcTemplate = new JdbcTemplate(datasouce);
            List<?> resultList = jdbcTemplate.queryForList("select * from test");
            System.out.println("===>>>>>>>>>>>" + JSON.toJSONString(resultList));
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
