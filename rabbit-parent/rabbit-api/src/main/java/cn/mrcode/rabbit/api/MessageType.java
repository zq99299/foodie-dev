package cn.mrcode.rabbit.api;

/**
 * 消息类型
 *
 * @author mrcode
 * @date 2021/10/16 22:02
 */
public class MessageType {
    /**
     * 迅速消息：不需要保障消息的可靠性，也不需要做 confirm 确认
     */
    public final static String RAPID = "0";
    /**
     * 确认消息：不需要保障消息的可靠性，但是会做消息的 confirm 确认
     */
    public final static String CONFIRM = "1";
    /**
     * 可靠性消息：一定保障消息 100% 可靠性投递，不允许有任何消息的丢失
     * <pre>
     *     保障数据库和所发的消息是原子性的（最终一致）
     * </pre>
     */
    public final static String RELIANT = "2";

}
