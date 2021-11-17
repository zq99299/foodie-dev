package cn.mrcode.esjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mrcode
 * @date 2021/11/16 22:09
 */
@Configuration
@ConditionalOnExpression("'${zookeeper.address}'.length() > 0")
public class RegistryCenterConfig {
    /**
     * 将 zk 注册中心加载到 spring 容器中
     * <pre>
     * initMethod ： 表示返回对象后，由 spirng 框架调用该对象的哪一个方法？这个不常用，因为可以在声明中直接调用
     *              这里要调用的就是 com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter#init()
     * </pre>
     *
     * @param address
     * @param namespace
     * @param connectionTimeout
     * @param sessionTimeout
     * @param maxRetries
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(
            @Value("${zookeeper.address}") final String address,
            @Value("${zookeeper.namespace}") final String namespace,
            @Value("${zookeeper.connectionTimeout}") final int connectionTimeout,
            @Value("${zookeeper.sessionTimeout}") final int sessionTimeout,
            @Value("${zookeeper.maxRetries}") final int maxRetries
    ) {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(address, namespace);
        zkConfig.setConnectionTimeoutMilliseconds(connectionTimeout);
        zkConfig.setSessionTimeoutMilliseconds(sessionTimeout);
        zkConfig.setMaxRetries(maxRetries);
        return new ZookeeperRegistryCenter(zkConfig);
    }
}
