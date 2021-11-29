package cn.mrcode.rabbit.producer.task;

import cn.mrcode.rabbit.producer.broker.RabbitBroker;
import cn.mrcode.rabbit.producer.constant.BrokerMessageStatus;
import cn.mrcode.rabbit.producer.entity.BrokerMessage;
import cn.mrcode.rabbit.producer.mapper.BrokerMessageMapper;
import cn.mrcode.rabbit.producer.service.MessageStoreService;
import cn.mrcode.rabbit.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/11/29 21:38
 */
@Component
/*
 * 这里 shardingTotalCount = 1，是因为考虑到我们这个消息组件是跟着 业务项目 走的，
 * 一个业务项目一张消息日志表，所以数据量不会很大，一个任务处理就行了
 * 如果说，你的数据量非常庞大，一张表肯定是搞不定的，可以分 n 表，然后设置分片来执行，一片执行其中的一部分表
 */
@ElasticJobConfig(
        jobName = "cn.mrcode.rabbit.producer.task.RetryMessageDataflowJob",
        cron = "0/10 * * * * ?",
        overwrite = true,
        description = "可靠性投递消息补偿任务",
        shardingTotalCount = 1
)
@Slf4j
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {
    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        // 只会抓取到可靠性消息，因为只有可靠性消息才会入库
        List<BrokerMessage> list = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("抓取数据集合，数量：{}", list.size());
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> data) {
        for (BrokerMessage message : data) {
            // 最开始为 0 ，失败一次，会增加一次
            // 达到最大重试次数后，该消息就最终失败了
            if (message.getTryCount() >= MAX_RETRY_COUNT) {
                messageStoreService.fail(message.getMessageId());
                continue;
            }
            // 还可以进行重试，则先更新一次重试数量，然后发起重新投递消息
            messageStoreService.updateTryCount(message.getMessageId());
            rabbitBroker.reliantSend(message.getMessage());
        }
    }
}
