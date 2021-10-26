package cn.mrcode.rabbit.common.converter;

import cn.mrcode.rabbit.common.serializer.Serializer;
import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 基础转换器
 * <pre>
 *    写出：将我们自己的消息对象 转换为 amqp 的消息对象
 *    接收：将 amqp 的消息对象转换为我们自己的消息对象
 * </pre>
 *
 * @author mrcode
 * @date 2021/10/26 22:34
 */
public class GenericMessageConverter implements MessageConverter {
    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }


    /**
     * 将我们的消息对象，转化为 org.springframework.amqp.core.message 的消息对象
     *
     * @param object            我们自己的消息对象
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(this.serializer.serializerRaw(object), messageProperties);
    }

    /**
     * 将 org.springframework.amqp.core.message 转化为我们自己的消息对象
     *
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.serializer.deserialize(message.getBody());
    }
}
