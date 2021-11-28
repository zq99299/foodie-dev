package cn.mrcode.rabbit.task.parser;

import cn.mrcode.rabbit.task.annotation.ElasticJobConfig;
import cn.mrcode.rabbit.task.autoconfigure.JobZookeeperProperties;
import cn.mrcode.rabbit.task.enums.ElasticJobTypeEnum;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author mrcode
 * @date 2021/11/25 21:30
 */
@Slf4j
public class ElasticJobConfParser implements ApplicationListener<ApplicationReadyEvent> {
    private JobZookeeperProperties jobZookeeperProperties;
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfParser(JobZookeeperProperties jobZookeeperProperties,
                                ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.jobZookeeperProperties = jobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();

        try {

            // 获取 spring 容器中，类上有 ElasticJobConfig 注解的对象
            Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ElasticJobConfig.class);
            for (Object bean : beanMap.values()) {
                Class<?> clazz = bean.getClass();
                // 这一个判断不知道在做什么
                if (clazz.getName().indexOf("$") > 0) {
                    String className = clazz.getName();
                    clazz = Class.forName(className.substring(0, className.indexOf("$")));
                }

                ElasticJobConfig conf = clazz.getAnnotation(ElasticJobConfig.class);
                String jobClass = clazz.getName();
                String jobName = conf.jobName();
                String cron = conf.cron();
                String shardingItemParameters = conf.shardingItemParameters();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String jobExceptionHandler = conf.jobExceptionHandler();
                String executorServiceHandler = conf.executorServiceHandler();
                boolean disabled = conf.disabled();

                String jobShardingStrategyClass = conf.jobShardingStrategyClass();
                String eventTraceRdbDataSource = conf.eventTraceRdbDataSource();
                String scriptCommandLine = conf.scriptCommandLine();

                boolean failover = conf.failover();
                boolean misfire = conf.misfire();
                boolean overwrite = conf.overwrite();
                boolean monitorExecution = conf.monitorExecution();
                boolean streamingProcess = conf.streamingProcess();

                int shardingTotalCount = conf.shardingTotalCount();
                int monitorPort = conf.monitorPort();
                int maxTimeDiffSeconds = conf.maxTimeDiffSeconds();
                int reconcileIntervalMinutes = conf.reconcileIntervalMinutes();

                // 构建 JOB 核心配置
                JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
                        .description(description)
                        .failover(failover)
                        .jobParameter(jobParameter)
                        .shardingItemParameters(shardingItemParameters)
                        .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                        .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                        .misfire(misfire)
                        .build();

                // 构建 JOB TYPE 配置
                // 获取接口类型，得到是什么类型的 JOB
                // 比如：MySimpleJob implements SimpleJob , 获取到的就是 SimpleJob
                // 这里简写，有可能这个 JOB 不止实现这一个接口或类
                String jobTypeName = clazz.getInterfaces()[0].getSimpleName();
                JobTypeConfiguration typeConfiguration = null;
                if (ElasticJobTypeEnum.SIMPLE.getType().equals(jobTypeName)) {
                    typeConfiguration = new SimpleJobConfiguration(coreConfig, jobClass);
                } else if (ElasticJobTypeEnum.DATAFLOW.getType().equals(jobTypeName)) {
                    typeConfiguration = new DataflowJobConfiguration(coreConfig, jobClass, streamingProcess);
                } else if (ElasticJobTypeEnum.SCRIPT.getType().equals(jobTypeName)) {
                    typeConfiguration = new ScriptJobConfiguration(coreConfig, scriptCommandLine);
                }

                // 构建 lite 配置
                LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                        .newBuilder(typeConfiguration)
                        .jobShardingStrategyClass(jobShardingStrategyClass)
                        .monitorExecution(monitorExecution)
                        .monitorPort(monitorPort)
                        .maxTimeDiffSeconds(maxTimeDiffSeconds)
                        .overwrite(overwrite)
                        .disabled(disabled)
                        .reconcileIntervalMinutes(reconcileIntervalMinutes)
                        .build();

                // 与 Spring 整合
                // 创建一个 Spring 的 BeanDefinition
                // new SpringJobScheduler(elasticJob,regCenter,jobConfig,jobEventConfig,..elasticJobListeners)
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
//                factory.setInitMethodName("init");
                factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);

                // 添加 Bean 的构造参数  SpringJobScheduler 的函数的参数
                if (!ElasticJobTypeEnum.SCRIPT.getType().equals(jobTypeName)) {
                    // 对于这里的 script 的封装，我觉得可能会有问题
                    // 因为 SpringJobScheduler 构造函数里面 少这一个参数的话，就匹配不上了
                    factory.addConstructorArgValue(bean);
                }
                factory.addConstructorArgValue(zookeeperRegistryCenter);
                factory.addConstructorArgValue(liteJobConfiguration);

                // 如果有 事件追踪配置
                // 也就是 JobEventRdbConfiguration(dataSource)
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    // 数据源的 Bean 名称
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    factory.addConstructorArgValue(rdbFactory);
                }

                // 添加监听
                List<BeanDefinition> targetElasticJobListeners = getTargetElasticJobListeners(conf);
                factory.addConstructorArgValue(targetElasticJobListeners);

                // 将 BeanDefinition 注入到 spring 容器中
                // 它可以将我们定义的 bean 中如果还有其他的 Autowire 等 spring 管理的注入需求，然后注入进去
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                String registerBeanName = conf.jobName() + "SpringJobScheduler";
                defaultListableBeanFactory.registerBeanDefinition(
                        registerBeanName,
                        factory.getBeanDefinition());

                // 注册 bean 定义之后，就可以在 Spring 容器中拿到刚刚注册的 bean 了
                SpringJobScheduler springJobScheduler = (SpringJobScheduler) applicationContext.getBean(registerBeanName);
                // 上面设置过 factory.setInitMethodName("init"); 这里就不要再次调用了，
                 springJobScheduler.init();
                log.info("启动 elastic-job 作业 {}", jobName);
            }
            log.info("共启动 elastic-job 作业数量 {} 个", beanMap.size());
        } catch (Exception e) {
            log.error("elasticjob 启动一次，系统强制退出", e);
            System.exit(1);
        }
    }

    /**
     * 构建监听
     *
     * @param conf
     * @return
     */
    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConfig conf) {
        List<BeanDefinition> result = new ManagedList<>(2);
        String listenerClass = conf.listenerClass();
        if (StringUtils.hasText(listenerClass)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listenerClass);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }

        String distributedListener = conf.distributedListener();
        if (StringUtils.hasText(distributedListener)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListener);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(conf.startedTimeoutMilliseconds());
            factory.addConstructorArgValue(conf.completedTimeoutMilliseconds());
            result.add(factory.getBeanDefinition());
        }
        return result;
    }
}
