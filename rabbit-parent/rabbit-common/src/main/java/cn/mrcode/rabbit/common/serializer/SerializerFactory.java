package cn.mrcode.rabbit.common.serializer;

/**
 * 序列化工厂
 * @author mrcode
 * @date 2021/10/26 22:09
 */
public interface SerializerFactory {
    Serializer create();
}
