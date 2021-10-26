package cn.mrcode.rabbit.common.serializer.impl;

import cn.mrcode.rabbit.api.Message;
import cn.mrcode.rabbit.common.serializer.Serializer;
import cn.mrcode.rabbit.common.serializer.SerializerFactory;

/**
 * Jackson 序列化工厂
 *
 * @author mrcode
 * @date 2021/10/26 22:30
 */
public class JacksonSerializerFactory implements SerializerFactory {
    public final static SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        // 使用我们自己定义的 Message 类创建属于该类专属的序列化\反序列化类
        return JacksonSerializer.createParametricType(Message.class);
    }
}
