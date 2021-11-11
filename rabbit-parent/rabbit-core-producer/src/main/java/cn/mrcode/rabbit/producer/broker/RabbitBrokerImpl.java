package cn.mrcode.rabbit.producer.broker;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.api.MessageType;
import cn.mrcode.rabbit.producer.constant.BrokerMessageConstant;
import cn.mrcode.rabbit.producer.constant.BrokerMessageStatus;
import cn.mrcode.rabbit.producer.entity.BrokerMessage;
import cn.mrcode.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author mrcode
 * @date 2021/10/20 22:17
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {
    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;
    @Autowired
    private MessageStoreService messageStoreService;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法
     *
     * @param message
     */
    private void sendKernel(Message message) {
        // 使用线程池发送消息
        AsyncBaseQueue.submit(() -> {
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            // 消息唯一 ID, 基础框架再次封装：xx#xx 的格式
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s",
                    message.getMessageId(),
                    System.currentTimeMillis()));

            // 从池中获取
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        // 1. 记录数据库的消息日志
        Date now = new Date();
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        // 尝试重试的次数，初始为 0
        brokerMessage.setTryCount(0);
        // 下一次重试时间
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConstant.TIMEOUT));

        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);

        message.setMessageType(MessageType.RELIANT);
        // 2. 发送消息
        sendKernel(message);
    }
}
