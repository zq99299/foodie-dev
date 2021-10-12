package cn.mrcode.foodiedev.rabbit.producer.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * 消息发送
 *
 * @author mrcode
 * @date 2021/10/12 21:59
 */
@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * <pre>
     * 消息确认，程序将消息投递到 MQ 后，MQ 响应一个成功事件；
     * 用于确认 MQ 是否已经收到消息
     * </pre>
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         *
         * @param correlationData 作为一个唯一的消息标识
         * @param ack broker 是否落盘成功，true 成功
         * @param cause 对应的失败异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("confirm：correlationData:" + correlationData + " ; ack=" + ack + " ; cause=" + cause);
        }
    };

    /**
     * 堆外发送消息的方法
     *
     * @param message    具体的消息内容
     * @param properties 额外的附加属性
     */
    public void send(Object message, Map<String, Object> properties) {
        // 创建消息头
        MessageHeaders mhs = new MessageHeaders(properties);
        // 创建消息
        Message<Object> msg = MessageBuilder.createMessage(message, mhs);

        // 设置  生产者回调，注意这里：不是每次都需要设置
        // 里面源码逻辑是：回调对象为 null 或则 此次设置的回调对象必须是上一次的回调对象 才会成功，否则会抛出异常
        rabbitTemplate.setConfirmCallback(confirmCallback);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                // 暂时没有搞懂这个 processor 的作用
                // 经过 convertAndSend 的源码调试
                // 在发送前会调用这个方法，但是这里 message 中的 headers 是空的，就算我们发送消息的时候传递了 headers，也不会在该对象中
                // 大概看了下源码：这里返回的 headers 是 new 了一个空的，原来我们自己发送的消息和 headers 被序列化成了消息传递出去
                System.err.println("---> post todo：message:" + message);
                return message;
            }
        };
        rabbitTemplate.convertAndSend(
                // 发往的 exchange
                "exchange-1",
                // routingKey
                "springboot.rabbitmq",
                // 消息
                msg,
                messagePostProcessor,
                // 消息唯一 ID: 指定业务的唯一 ID
                correlationData);
    }
}
