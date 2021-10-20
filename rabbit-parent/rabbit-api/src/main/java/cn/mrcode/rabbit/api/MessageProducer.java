package cn.mrcode.rabbit.api;

import cn.mrcode.rabbit.api.exception.MessageRunTimeException;

import java.util.List;

/**
 * 消息发送
 *
 * @author mrcode
 * @date 2021/10/16 22:23
 */
public interface MessageProducer {
    void send(Message message) throws MessageRunTimeException;

    /**
     * 消息发送，附带 callback 回调执行相应业务逻辑
     *
     * @param message
     * @param sendCallback
     * @throws MessageRunTimeException
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

    void send(List<Message> messages) throws MessageRunTimeException;
}
