package cn.mrcode.esjob.config;

import cn.mrcode.esjob.listener.SimpleJobListener;
import cn.mrcode.esjob.task.MySimpleJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 简单任务 配置
 *
 * @author mrcode
 * @date 2021/11/16 22:30
 */
@Configuration
public class MySimpleJobConfig {
    // zk 注册中心
    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    // 运行日志监听器
    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    /**
     * 我们自己写的 JOB ，用来执行业务逻辑
     * @return
     */
    @Bean
    public SimpleJob simpleJob() {
        return new MySimpleJob();
    }

    /**
     * 调度器配置
     * @param simpleJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @param jobParameter
     * @param failover
     * @param monitorExecution
     * @param monitorPort
     * @param maxTimeDiffSeconds
     * @param jobShardingStrategyClass
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(
            final SimpleJob simpleJob,
            @Value("${simpleJob.cron}") final String cron,
            @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters,
            @Value("${simpleJob.jobParameter}") final String jobParameter,
            @Value("${simpleJob.failover}") final boolean failover,
            @Value("${simpleJob.monitorExecution}") boolean monitorExecution,
            @Value("${simpleJob.monitorPort}") final int monitorPort,
            @Value("${simpleJob.maxTimeDiffSeconds}") final int maxTimeDiffSeconds,
            @Value("${simpleJob.jobShardingStrategyClass}") final String jobShardingStrategyClass
    ) {
        return new SpringJobScheduler(
                // 作业对象
                simpleJob,
                // 注册中心
                zookeeperRegistryCenter,
                // 作业配置
                getLiteJobConfiguration(
                        simpleJob.getClass(),
                        cron,
                        shardingTotalCount,
                        shardingItemParameters,
                        jobParameter,
                        failover,
                        monitorExecution,
                        monitorPort,
                        maxTimeDiffSeconds,
                        jobShardingStrategyClass
                ),
                // 作业事件监听器，框架提供的一些可以持久化策略的监听器配置
                jobEventConfiguration,
                // 作业监听器，作业运行之前，运行之后得到一些信息
                new SimpleJobListener()
        );
    }

    /**
     * 针对该作业的配置
     *
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @param jobParameter
     * @param failover
     * @param monitorExecution
     * @param monitorPort
     * @param maxTimeDiffSeconds
     * @param jobShardingStrategyClass
     * @return
     */
    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass,
                                                         String cron,
                                                         int shardingTotalCount,
                                                         String shardingItemParameters,
                                                         String jobParameter,
                                                         boolean failover,
                                                         boolean monitorExecution,
                                                         int monitorPort,
                                                         int maxTimeDiffSeconds,
                                                         String jobShardingStrategyClass) {
        // 作业核心配置
        JobCoreConfiguration coreConfig = JobCoreConfiguration
                // jobName – 作业名称
                // cron – 作业启动时间的 cron 表达式
                // shardingTotalCount – 作业分片总数
                .newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .misfire(true)
                .failover(failover)
                .jobParameter(jobParameter)
                .shardingItemParameters(shardingItemParameters)
                .build();

        // 简单作业配置
        SimpleJobConfiguration jobConfig = new SimpleJobConfiguration(coreConfig, jobClass.getName());
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                .newBuilder(jobConfig)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                // 设置本地配置是否可覆盖注册中心配置.
                // 如果可覆盖, 每次启动作业都以本地配置为准
                // 可能在 zk 上已经存在该配置了（以前注册过，比如再次重启该服务，就会注册一次），以 zk 上的为准，还是以本地的为主
                // 以 zk 为主的话，一般使用场景是：使用控制台添加，任务，直接在 zk 上添加配置信息
                .overwrite(false)
                .build();
        return liteJobConfiguration;
    }
}
