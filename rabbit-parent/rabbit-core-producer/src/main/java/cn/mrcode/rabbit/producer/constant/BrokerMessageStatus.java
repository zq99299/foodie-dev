package cn.mrcode.rabbit.producer.constant;

/**
 * 消息日志状态
 *
 * @author mrcode
 * @date 2021/11/11 20:42
 */
public enum BrokerMessageStatus {
    SENDING("0"),
    SEND_OK("1"),
    SEND_FALL("2"),
    /**
     * 不是最终的失败，还可以重试
     * <pre>
     *     对于是否可以重试要根据具体的原因来定义，比如：
     *     - broker 磁盘满了，再重试也没有任何意义
     *     - broker os cache 满了，重试也没有任何意义
     *    简单说，这个失败有很多中清空，可以根据具体的场景来定制，是否需要进行重试
     * </pre>
     */
    SEND_FALL_A_MOMENT("3");
    private String code;

    BrokerMessageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
