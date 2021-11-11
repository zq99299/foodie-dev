package cn.mrcode.rabbit.producer.constant;

/**
 * @author mrcode
 * @date 2021/11/11 20:46
 */
public interface BrokerMessageConstant {
    /**
     * 消息投递后，超时多久未收到确认则认为该消息需要重试？，单位 分钟
     */
    int TIMEOUT = 1;
}
