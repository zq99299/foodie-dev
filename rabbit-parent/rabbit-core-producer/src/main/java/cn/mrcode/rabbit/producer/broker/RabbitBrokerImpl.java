package cn.mrcode.rabbit.producer.broker;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.api.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrcode
 * @date 2021/10/20 22:17
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {

    }

    @Override
    public void reliantSend(Message message) {

    }
}
