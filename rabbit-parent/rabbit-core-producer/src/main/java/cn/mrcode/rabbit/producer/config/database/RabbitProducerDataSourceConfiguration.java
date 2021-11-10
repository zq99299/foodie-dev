package cn.mrcode.rabbit.producer.config.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @author mrcode
 * @date 2021/11/9 22:08
 */
@Configuration
// 加载 classpath 下的 rabbit-producer-message.properties 文件
@PropertySource({"classpath:rabbit-producer-message.properties"})
public class RabbitProducerDataSourceConfiguration {
    private static Logger LOGGER = LoggerFactory.getLogger(RabbitProducerDataSourceConfiguration.class);

    // 获取配置文件中的 数据源 class
    // 这里 spring 居然可以将类路径转换为 class
    @Value("${rabbit.producer.druid.type}")
    private Class<? extends DataSource> dataSourceType;

    // 构建 数据源
    @Bean(name = "rabbitProducerDataSource")
    @Primary
    // 读取文件中 rabbit.producer.druid.jdbc 的内容填充到 返回的数据源中的配置属性
    @ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")
    public DataSource rabbitProducerDataSource() {
        // 使用 boot 包中的 jdbc 工具创建数据源对象
        DataSource rabbitProducerDataSource = DataSourceBuilder.create().type(dataSourceType).build();
        LOGGER.info("============== rabbitProducerDataSource : {} ==============", rabbitProducerDataSource);
        return rabbitProducerDataSource;
    }
}
