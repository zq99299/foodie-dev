package cn.mrcode.rabbit.producer.config.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * 程序启动时，执行 SQL 脚本，创建 broker_message 表
 * @author mrcode
 * @date 2021/11/9 22:07
 */
@Configuration
public class BrokerMessageConfiguration {
    @Autowired
    private DataSource rabbitProducerDataSource;

    // 注意这里的 Resource 是 org.springframework.core.io.Resource 的
    @Value("classpath:rabbit-producer-message-schema.sql")
    private Resource schemaScript;

    /**
     * 用于在初始化过程中设置数据库，并在销毁过程中清理数据库。
     *
     * @return
     */
    @Bean
    public DataSourceInitializer initDataSourceInitializer() {
        System.err.println("----------rabbitProducerDataSource:" + rabbitProducerDataSource);
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(rabbitProducerDataSource);
        // 在程序初始化阶段执行
        initializer.setDatabasePopulator(databasePopulator());
        // 这个则在程序销毁阶段执行
        // initializer.setDatabaseCleaner();
        return initializer;
    }

    /**
     * 用于填充、初始化或清理数据库的策略
     * @return
     */
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}
