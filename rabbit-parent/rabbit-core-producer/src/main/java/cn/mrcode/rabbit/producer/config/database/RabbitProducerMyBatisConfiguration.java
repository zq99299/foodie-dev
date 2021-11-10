package cn.mrcode.rabbit.producer.config.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author mrcode
 * @date 2021/11/9 22:08
 */
@Configuration
// 在 数据库脚本 配置类完成之后，再初始化该类
@AutoConfigureAfter(value = {RabbitProducerDataSourceConfiguration.class})
public class RabbitProducerMyBatisConfiguration {

    @Bean(name = "rabbitProducerSqlSessionFactory")
    public SqlSessionFactory rabbitProducerSqlSessionFactory(DataSource rabbitProducerDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        try {
            bean.setDataSource(rabbitProducerDataSource);
            // 资源匹配器，将给定的路径转换为资源
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // 使用资源匹配器扫描到指定路径下的所有符合条件的资源
            // 这里设置的是 mapper.xml 的路径
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

            SqlSessionFactory sqlSessionFactory = bean.getObject();
            // 开启缓存？
            sqlSessionFactory.getConfiguration().setCacheEnabled(Boolean.TRUE);
            return sqlSessionFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 需要事物的话，需要返回这个模板；
     * 这里笔者暂时不知道有什么用
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "rabbitProducerSqlSessionTemplate")
    public SqlSessionTemplate rabbitProducerSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
