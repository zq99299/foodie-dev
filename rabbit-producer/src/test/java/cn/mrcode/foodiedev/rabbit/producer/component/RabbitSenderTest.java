package cn.mrcode.foodiedev.rabbit.producer.component;

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
    private RabbitSender rabbitSender;

    @Test
    public void testSender() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("attr1", "12345");
        properties.put("attr2", "abcd1");
        rabbitSender.send("hello rabbitmq!", properties);

        // 休眠 15 秒，因为有一些事件回调，等待他们的回调
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}