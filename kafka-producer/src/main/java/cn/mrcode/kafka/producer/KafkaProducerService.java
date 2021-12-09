package cn.mrcode.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 生产端
 * @author mrcode
 * @date 2021/12/9 21:21
 */
@Slf4j
@Component
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     * @param topic
     * @param message 这里提供 Object，由于前面设置了 value 的序列化，这里会将 object 序列化为 字符串，实际上应该会调用
     */
    public void sendMessage(String topic, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            /**
             * 失败时
             * @param ex
             */
            @Override
            public void onFailure(Throwable ex) {
                log.info("发送消息失败：{}", ex.getMessage());
            }

            /**
             * 成功时
             * @param result
             */
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("发送消息成功：{}", result);
            }
        });
    }
}
