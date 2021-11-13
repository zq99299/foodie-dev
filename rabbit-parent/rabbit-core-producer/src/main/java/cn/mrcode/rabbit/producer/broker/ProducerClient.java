package cn.mrcode.rabbit.producer.broker;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.api.MessageProducer;
import cn.mrcode.rabbit.api.MessageType;
import cn.mrcode.rabbit.api.SendCallback;
import cn.mrcode.rabbit.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/10/20 21:52
 */
@Component
public class ProducerClient implements MessageProducer {
    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        // 如果 topic 为 null 就抛出空指针异常
        String topic = message.getTopic();
        Preconditions.checkNotNull(topic);
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
        }
    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
