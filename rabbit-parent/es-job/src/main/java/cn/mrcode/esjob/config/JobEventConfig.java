package cn.mrcode.esjob.config;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 作业事件监听器, 配置此监听器之后，会有一些作业运行的日志信息存储在数据库中，默认的不是很详细，可以自己定制开发重写实现更纤细的日志监控
 *
 * @author mrcode
 * @date 2021/11/16 22:33
 */
@Configuration
public class JobEventConfig {
    /**
     * 配置文件中直接用 spring.datasource 配置一个数据源就可以了
     */
    @Autowired
    private DataSource dataSource;

    @Bean
    public JobEventConfiguration jobEventConfiguration() {
        return new JobEventRdbConfiguration(dataSource);
    }
}
