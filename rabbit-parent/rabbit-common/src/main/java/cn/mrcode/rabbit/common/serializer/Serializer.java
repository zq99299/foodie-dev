package cn.mrcode.rabbit.common.serializer;

/**
 * 序列化和反序列化接口
 *
 * @author mrcode
 * @date 2021/10/26 22:09
 */
public interface Serializer {
    /**
     * 将消息对象转换为 byte[]
     *
     * @param data
     * @return
     */
    byte[] serializerRaw(Object data);

    /**
     * 将消息转换为 string
     *
     * @param data
     * @return
     */
    String serialize(Object data);

    /**
     * 将字符串转换为 我们自己的消息对象
     *
     * @param content
     * @param <T>     这里设置为泛型是为了方便外部灵活使用
     * @return
     */
    <T> T deserialize(String content);

    /**
     * 将数组反序列化为我们自己的消息对象
     *
     * @param content
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] content);
}
