package cn.mrcode.rabbit.producer.broker;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.api.MessageType;
import cn.mrcode.rabbit.api.exception.MessageRunTimeException;
import cn.mrcode.rabbit.common.converter.GenericMessageConverter;
import cn.mrcode.rabbit.common.converter.RabbitMessageConverter;
import cn.mrcode.rabbit.common.serializer.Serializer;
import cn.mrcode.rabbit.common.serializer.SerializerFactory;
import cn.mrcode.rabbit.common.serializer.impl.JacksonSerializerFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * RabbitTemplate 池化封装
 * <pre>
 *    每一个 topic（交换机）对应一个 RabbitTemplate
 *    1. 提高发送效率
 *    2. 可以根据不同的需求制定不同的 RabbitTemplate
 * </pre>
 *
 * @author mrcode
 * @date 2021/10/25 22:26
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {
    private Map<String /* TOPIC */, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();
    @Autowired
    private ConnectionFactory connectionFactory;
    // 构建一个分割器，以 # 分隔
    private Splitter splitter = Splitter.on("#");

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    /**
     * 根据消息为每一个 topic（交换器）产生一个 RabbitTemplate
     *
     * @param message
     * @return
     * @throws MessageRunTimeException
     */
    public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic:{} is null", topic);
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());

        // 对 message 的序列化: 使用我们自己的消息转换器
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter genericMessageConverter = new GenericMessageConverter(serializer);
        RabbitMessageConverter rabbitMessageConverter = new RabbitMessageConverter(genericMessageConverter);
        newRabbitTemplate.setMessageConverter(rabbitMessageConverter);

        String messageType = message.getMessageType();
        // 只要不是迅速消息，就设置上：发送方法回调确认
        if (!MessageType.RAPID.equals(messageType)) {
            newRabbitTemplate.setConfirmCallback(this);
        }
        // 如果 key 不存在就 put，存在则不会 put
        rabbitMap.putIfAbsent(topic, newRabbitTemplate);
        return newRabbitTemplate;
    }

    /**
     * MQ 收到消息后，回调发送方的 通知方法；
     * <pre>
     *     在我们的消息类别定义中，只要不是 迅速消息  都需要有发送确认 的操作
     * </pre>
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // correlationData  消息唯一 ID，在发送时，我们自己定义的格式是 消息ID#时间戳
        List<String> idItems = splitter.splitToList(correlationData.getId());
        String messageId = idItems.get(0);
        long sendTime = Long.parseLong(idItems.get(1));
        if (ack) {
            // ack 为 true 表示 MQ 已经确认收到消息
            log.info("#RabbitTemplateContainer.confirm# send message is OK, confirm messageId: {},sendTime: {}", messageId, sendTime);
        } else {
            log.error("#RabbitTemplateContainer.confirm# send message is Fail, confirm messageId: {},sendTime: {},cause: {}", messageId, sendTime, cause);
        }
    }
}
