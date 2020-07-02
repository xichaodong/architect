package com.chloe.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.*;
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

    final ConfirmCallback confirmCallback = new ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        }
    };

    public void send(Object payload, Map<String, Object> properties) {
        MessageHeaders headers = new MessageHeaders(properties);
        org.springframework.messaging.Message<?> msg = MessageBuilder.createMessage(payload, headers);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor messagePostProcessor = message -> {
            System.out.println("post message: " + message.toString());
            return message;
        };

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("mirror-ex", "mirror", msg, messagePostProcessor, correlationData);
    }
}
