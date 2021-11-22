package cn.mrcode.esjob.config;

import cn.mrcode.esjob.listener.SimpleJobListener;
import cn.mrcode.esjob.task.MyDataflowJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
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
 * @author mrcode
 * @date 2021/11/22 20:37
 */
@Configuration
public class DataFlowJobConfig {
    // zk 注册中心
    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    // 运行日志监听器
    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    @Bean
    public MyDataflowJob myDataflowJob() {
        return new MyDataflowJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler dataflowJobScheduler(
            MyDataflowJob myDataflowJob,
            @Value("${dataflowJob.cron}") final String cron,
            @Value("${dataflowJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${dataflowJob.shardingItemParameters}") final String shardingItemParameters
    ) {
        return new SpringJobScheduler(
                // 作业对象
                myDataflowJob,
                // 注册中心
                zookeeperRegistryCenter,
                // 作业配置
                getLiteJobConfiguration(
                        myDataflowJob.getClass(),
                        cron,
                        shardingTotalCount,
                        shardingItemParameters
                ),
                // 作业事件监听器，框架提供的一些可以持久化策略的监听器配置
                jobEventConfiguration
        );

    }

    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends DataflowJob> jobClass,
                                                         String cron,
                                                         int shardingTotalCount,
                                                         String shardingItemParameters) {

        return LiteJobConfiguration.newBuilder(
                        new DataflowJobConfiguration(
                                JobCoreConfiguration.newBuilder(
                                                jobClass.getName(),  // job 名称，全限定名称:cn.mrcode.xx
                                                cron,
                                                shardingTotalCount
                                        ).shardingItemParameters(shardingItemParameters)
                                        .build(),
                                // class 全限定名称 cn.mrcode.xx
                                jobClass.getCanonicalName(),
                                // streamingProcess 是否是流式处理
                                true
                        )
                )
                // 不覆盖 zk 配置
                .overwrite(false)
                .build();
    }
}
