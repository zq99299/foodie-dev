package cn.mrcode.rabbit.common.converter;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 扩展基础转换器，使用装饰者模式，持有基础转换器进行转换，在转换前可以做一些我们自己特有的逻辑处理
 *
 * @author mrcode
 * @date 2021/10/26 22:44
 */
public class RabbitMessageConverter implements MessageConverter {
    /**
     * 在基础转换器上做装饰
     */
    private GenericMessageConverter delegate;

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    /**
     * 写出消息
     *
     * @param object
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        // 在写出前，定制自己的逻辑
        // 比如，可以设置消息过期时间、设置成 utf8 格式等，messageProperties 中有很多 rabbit 相关的东西可以set
        // messageProperties.setExpiration(String.valueOf(1000 * 60));
        cn.mrcode.rabbit.api.Message message = (cn.mrcode.rabbit.api.Message) object;
        int delayMills = message.getDelayMills();
        // 如果设置了延迟时间，则增加延迟插件的头
        if (delayMills > 0) {
//            messageProperties.setHeader("x-delay", delayMills);
            // 上面手动设置等同于下面使用 API
            messageProperties.setDelay(delayMills);
        }
        return delegate.toMessage(object, messageProperties);
    }

    /**
     * 接收消息
     *
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        cn.mrcode.rabbit.api.Message msg = (cn.mrcode.rabbit.api.Message) delegate.fromMessage(message);
        return msg;
    }
}
