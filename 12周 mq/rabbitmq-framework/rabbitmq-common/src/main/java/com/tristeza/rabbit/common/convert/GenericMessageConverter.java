package com.tristeza.rabbit.common.convert;

import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.common.serialize.serializer.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/7/6 7:55 下午
 */
public class GenericMessageConverter implements MessageConverter {
    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        if (Objects.isNull(serializer)) {
            throw new MessageRunTimeException("序列化解析器为空");
        }

        this.serializer = serializer;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(this.serializer.serializer2Byte(object), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.serializer.deserializer(message.getBody());
    }
}
