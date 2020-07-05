package com.tristeza.rabbitmq.producer.pool;

import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2020/7/5 5:18 下午
 */
@Component
public class RabbitMQTemplatePool implements RabbitTemplate.ConfirmCallback {
    private Map<String, RabbitTemplate> pool = new ConcurrentHashMap<>();

    @Resource
    private ConnectionFactory connectionFactory;

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
//        newTemplate.setMessageConverter();

        int messageType = message.getMessageType();

        if (MessageType.RAPID != messageType) {
            newTemplate.setConfirmCallback(this);
        }

        pool.putIfAbsent(message.getTopic(), newTemplate);

        return pool.get(message.getTopic());
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
