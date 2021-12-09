package cn.mrcode.kafka.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author mrcode
 * @date 2021/12/9 21:54
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaProducerServiceTest {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    public void sendMessage() throws InterruptedException {
        String topic = "topic02";
        for (int i = 0; i < 10; i++) {
            kafkaProducerService.sendMessage(topic, "hello kafka" + i);
        }
        // 想要看到回调信息，就需要让测试不结束
        TimeUnit.MINUTES.sleep(10);
    }
}