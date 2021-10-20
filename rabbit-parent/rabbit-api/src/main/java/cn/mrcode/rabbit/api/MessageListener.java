package cn.mrcode.rabbit.api;

/**
 * 消息监听
 *
 * @author mrcode
 * @date 2021/10/16 22:27
 */
public interface MessageListener {
    void onMessage(Message message);
}
