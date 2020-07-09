package com.tristeza.rabbitmq.producer.pool;

import com.google.common.base.Splitter;
import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbit.common.convert.GenericMessageConverter;
import com.tristeza.rabbit.common.convert.RabbitMessageConverter;
import com.tristeza.rabbit.common.serialize.factory.SerializerFactory;
import com.tristeza.rabbit.common.serialize.factory.impl.JacksonSerializerFactory;
import com.tristeza.rabbitmq.producer.db.service.MessageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2020/7/5 5:18 下午
 */
@Component
public class RabbitMQTemplatePool implements RabbitTemplate.ConfirmCallback {
    private static Logger LOGGER = LoggerFactory.getLogger(RabbitTemplate.class);
    private static Splitter SPLITTER = Splitter.on("#");

    private Map<String, RabbitTemplate> pool = new ConcurrentHashMap<>();
    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private MessageStoreService messageStoreService;

    public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
        if (Objects.isNull(message)) {
            throw new MessageRunTimeException("池化时消息为空");
        }

        RabbitTemplate template = pool.get(message.getTopic());

        if (Objects.nonNull(template)) {
            return template;
        }

        RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
        newTemplate.setExchange(message.getTopic());
        newTemplate.setRetryTemplate(new RetryTemplate());
        newTemplate.setRoutingKey(message.getRoutingKey());

        GenericMessageConverter genericMessageConverter = new GenericMessageConverter(serializerFactory.create());
        newTemplate.setMessageConverter(new RabbitMessageConverter(genericMessageConverter));

        int messageType = message.getMessageType();

        if (MessageType.RAPID != messageType) {
            newTemplate.setConfirmCallback(this);
        }

        pool.putIfAbsent(message.getTopic(), newTemplate);

        return pool.get(message.getTopic());
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        List<String> strings = SPLITTER.splitToList(Objects.requireNonNull(correlationData.getId()));

        String messageId = strings.get(0);
        Long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);

        if (ack) {
            if (MessageType.RELIANT == Integer.parseInt(messageType)) {
                messageStoreService.success(messageId);
            }
            messageStoreService.success(messageId);
            LOGGER.info("发送消息成功, messageId:{}, 发送时间:{}", messageId, sendTime);
        } else {
            LOGGER.error("发送消息失败, messageId:{}, 发送时间:{}", messageId, sendTime);
        }
    }
}
