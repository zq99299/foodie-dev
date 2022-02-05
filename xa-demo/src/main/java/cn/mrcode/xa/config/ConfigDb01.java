package cn.mrcode.xa.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 第一个数据库的数据源配置
 *
 * @author mrcode
 * @date 2022/2/5 17:46
 */
@Configuration
/*
    扫描 mapper 接口, 目的是为了创建这个接口的代理
    由于这里有 2 个数据源，所以需要指定这个接口使用的数据源是哪一个
 */
@MapperScan(value = "cn.mrcode.xa.dao.db01.mapper",sqlSessionFactoryRef = "sqlSessionFactoryBeanDb01")
public class ConfigDb01 {
    @Bean
    public DataSource db01() {
        // 要使用 MySQL xa 数据源
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setURL("jdbc:mysql://localhost:3307/xa-01?serverTimezone=Asia/Shanghai&useSSL=true&characterEncoding=utf8");
        xaDataSource.setUser("root");
        xaDataSource.setPassword("root");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);

        return atomikosDataSourceBean;
    }

    /**
     * 配置 mybatis
     */
    @Bean("sqlSessionFactoryBeanDb01")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("db01") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 这里需要一个 Resource 可变数组，如何写？
        /* 其实这个可以通过查看他的自动配置源码是如何写的
            mybatis:
              mapper-locations: /mapper/*.xml
         */
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources("/mapper/db01/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean;
    }
}
