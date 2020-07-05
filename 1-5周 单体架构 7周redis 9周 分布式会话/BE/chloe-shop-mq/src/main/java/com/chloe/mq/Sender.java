package com.chloe.mq;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@Component
public class Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    final ConfirmCallback confirmCallback = (correlationData, ack, cause) ->
            System.out.println("Ack结果：" + ack + " id:" + correlationData.getId());

    public void send(Object payload, Map<String, Object> properties) {
        MessageHeaders headers = new MessageHeaders(properties);
        org.springframework.messaging.Message<?> msg = MessageBuilder.createMessage(payload, headers);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor messagePostProcessor = message -> {
            System.out.println("post message: " + message.toString());
            return message;
        };

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("mirror-ex", "mirror.123", msg, messagePostProcessor, correlationData);
    }
}
