package cn.mrcode.rabbit.producer.config.database;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mrcode
 * @date 2021/11/9 22:09
 */
@Configuration
@AutoConfigureAfter(RabbitProducerMyBatisConfiguration.class)
public class RabbitProducerMybatisMapperScanerConfig {

    @Bean(name = "rabbitProducerMybatisMapperScanerConfigurer")
    public MapperScannerConfigurer rabbitProducerMybatisMapperScanerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 这个会话工厂是可选的，除非定义了多个会话工厂
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("rabbitProducerSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("cn.mrcode.rabbit.producer.mapper");
        return mapperScannerConfigurer;
    }
}
