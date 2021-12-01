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
        /**
         * 这个方法我不想写了，因为视频中讲这个我觉得完全在堆技术；
         * 1. 首先底层没有批量发送一说
         * 2. 他强制会把消息类型变成 快速消息，也就是说，默认这个方法只能发送快速消息
         * 3. 这里使用 ThreadLocal 方法，循环方式将 message 添加到里面的 list 中
         * 4. 然后调用 rabbitBroker.sendMessage() ,注意这里是不带参数的
         * 5. rabbitBroker.sendMessage() 里面的实现居然是从 ThreadLocal 里面取出来一个 list 然后循环单个调用消息发送
         *    全是同步操作，而且直接调用，那为什么我不直接把消息直接通过方法形参传递进去？
         * 6. 最后有一点我认同：批量消息重新搞了个内存队列和线程来处理，和单个的分开了
         *
         * 鉴于以上 5 点，太离谱，不写这个实现了
         *
         */
    }
}
