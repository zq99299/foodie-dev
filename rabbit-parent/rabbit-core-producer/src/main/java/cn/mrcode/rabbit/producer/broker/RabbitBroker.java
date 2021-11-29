package cn.mrcode.rabbit.producer.broker;

import cn.mrcode.rabbit.api.Message;

/**
 * 具体发送不同类型消息的接口
 *
 * @author mrcode
 * @date 2021/10/20 22:11
 */
public interface RabbitBroker {
    /**
     * 迅速消息实现
     *
     * @param message
     */
    void rapidSend(Message message);

    void confirmSend(Message message);

    /**
     * 可靠性消息投递；会往数据库写入一条消息日志信息
     * @param message
     */
    void reliantSend(Message message);
}
