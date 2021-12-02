package cn.mrcode.test;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.api.MessageBuilder;
import cn.mrcode.rabbit.api.MessageType;
import cn.mrcode.rabbit.producer.broker.ProducerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author mrcode
 * @date 2021/10/12 22:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitSenderTest {
    @Autowired
    private ProducerClient producerClient;

    @Test
    public void testSender() {
        Message message = MessageBuilder.create()
                .withMessageType(MessageType.RELIANT)
                .withTopic("exchange-1")
                .withRoutingKey("springboot.rabbitmq")
                .build();
        producerClient.send(message);

        // 休眠 15 秒，因为有一些事件回调，等待他们的回调
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delayedSender() {
        Message message = MessageBuilder.create()
                .withMessageType(MessageType.RELIANT)
                .withTopic("delayed-exchange")
                .withRoutingKey("delay.abc")
                .withDelayMills(10000) // 10 秒后进入队列
                .build();
        producerClient.send(message);

        // 休眠 15 秒，因为有一些事件回调，等待他们的回调
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}