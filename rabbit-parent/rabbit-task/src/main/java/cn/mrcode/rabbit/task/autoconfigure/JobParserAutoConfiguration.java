package cn.mrcode.rabbit.task.autoconfigure;

import cn.mrcode.rabbit.task.parser.ElasticJobConfParser;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解析 JOB 的配置
 *
 * @author mrcode
 * @date 2021/11/24 21:53
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "elastic.job.zk",
        name = {"namespace", "server-lists"}, // 必须存在这两个属性才生效
        matchIfMissing = false  // 如果属性不存在，条件成立吗？默认就是不成立，也就是不生效
)
// 当前面的注解条件生效后，该注解才会生效，指定的配置扫描并初始化 JobZookeeperProperties 类
@EnableConfigurationProperties(JobZookeeperProperties.class)
public class JobParserAutoConfiguration {
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(JobZookeeperProperties jobZookeeperProperties) {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(
                jobZookeeperProperties.getServerLists(),
                jobZookeeperProperties.getNamespace());
        zkConfig.setConnectionTimeoutMilliseconds(jobZookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(jobZookeeperProperties.getSessionTimeoutMilliseconds());
        zkConfig.setMaxRetries(jobZookeeperProperties.getMaxRetries());
        zkConfig.setBaseSleepTimeMilliseconds(jobZookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setMaxSleepTimeMilliseconds(jobZookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setDigest(jobZookeeperProperties.getDigest());
        log.info("JOB 注册中心配置成功，zkServerLists={},namespace={}", jobZookeeperProperties.getNamespace(), jobZookeeperProperties.getNamespace());
        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    public ElasticJobConfParser elasticJobConfParser(JobZookeeperProperties jobZookeeperProperties) {
        return new ElasticJobConfParser(jobZookeeperProperties);
    }
}
