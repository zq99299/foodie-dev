package cn.mrcode.rabbit.task.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author mrcode
 * @date 2021/11/24 22:40
 */
@ConfigurationProperties(prefix = "elastic.job.zk")
@Data
public class JobZookeeperProperties {
    /**
     * 连接Zookeeper服务器的列表
     * 包括IP地址和端口号
     * 多个地址用逗号分隔
     * 如: host1:2181,host2:2181
     */
    private String serverLists;
    /**
     * Zookeeper的命名空间
     */
    private String namespace;
    /**
     * 等待重试的间隔时间的初始值
     * 单位：毫秒
     */
    private int baseSleepTimeMilliseconds = 1000;
    /**
     * 等待重试的间隔时间的最大值
     * 单位：毫秒
     */
    private int maxSleepTimeMilliseconds = 3000;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
    /**
     * 连接超时时间
     * 单位：毫秒
     */
    private int connectionTimeoutMilliseconds = 15000;
    /**
     * 会话超时时间
     * 单位：毫秒
     */
    private int sessionTimeoutMilliseconds = 60000;
    /**
     * 连接 Zookeeper 的权限令牌
     * 缺省为不需要权限验证
     */
    private String digest = "";
}
