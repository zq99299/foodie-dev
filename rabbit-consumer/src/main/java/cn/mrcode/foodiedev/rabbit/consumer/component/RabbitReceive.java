package cn.mrcode.foodiedev.rabbit.consumer.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 消息消费者
 *
 * @author mrcode
 * @date 2021/10/12 21:59
 */
@Component
public class RabbitReceive {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(
            // 队列与 exchange 的绑定关系，与一些持久化配置等
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-1", durable = "true"),
                    exchange = @Exchange(
                            name = "exchange-1",
                            durable = "true",
                            type = ExchangeTypes.TOPIC,
                            // 忽略声明异常
                            ignoreDeclarationExceptions = "true"),
                    // routingKey: 这里由于是 topic 模式，所以支持通配符
                    key = "springboot.*"
            )
    )
    // 如果说 上面的这些名称，写到配置文件中的话，这里如何赋值？直接使用 boot 提供的 ${xxx} 就可以了
    // @RabbitListener(bindings = @QueueBinding(@Queue(value = "${my.rabbit.queue-1}", durable = "true")))
    public void onMessage(Message message, Channel channel) throws IOException {
        // 1. 收到消息以后进行业务端消费处理
        System.err.println("--------------------------");
        System.err.println("消费消息：" + message.getPayload());

        // 2. 处理手动 ack 操作
        // 需要手工 ack, 单条消息确认
        // 消息到达 MQ 之后，会给每一条消息一个唯一的 deliveryTag
        long deliveryTag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);

    }
}
