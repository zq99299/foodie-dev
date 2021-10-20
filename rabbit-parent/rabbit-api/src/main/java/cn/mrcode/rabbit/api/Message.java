package cn.mrcode.rabbit.api;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrcode
 * @date 2021/10/16 21:56
 */
@Data
public class Message implements Serializable {
    /**
     * 消息唯一 ID
     */
    private String messageId;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息的路由规则
     */
    private String routingKey = "";
    /**
     * 消息的附加属性
     */
    private Map<String, Object> attributes = new HashMap<>();
    /**
     * 延迟消息 时间
     */
    private int delayMills;
    /**
     * 消息类型
     */
    private String messageType = MessageType.CONFIRM;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills, String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }
}
