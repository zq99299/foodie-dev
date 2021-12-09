package cn.mrcode.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 消费端
 *
 * @author mrcode
 * @date 2021/12/9 21:36
 */
@Component
@Slf4j
public class KafkaConsumerService {
    @KafkaListener(groupId = "group02", topics = "topic02")
    public void onMessage(ConsumerRecord<String, Object> record,
                          Acknowledgment acknowledgment,
                          Consumer<?, ?> consumer) {
        log.info("消费端接受消息：{}", record);
        // 手工签收
        acknowledgment.acknowledge();
        log.info("查看消费端的一些信息，比如当前线程，消费端的配置等信息：{}", consumer);
    }
}
