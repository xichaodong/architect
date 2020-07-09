package com.tristeza.rabbitmq.producer.client;

import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbit.api.producer.MessageProducer;
import com.tristeza.rabbit.api.producer.SendCallback;
import com.tristeza.rabbitmq.producer.async.AsyncQueue;
import com.tristeza.rabbitmq.producer.broker.RabbitBroker;
import com.tristeza.rabbitmq.producer.holder.MessageHolder;
import com.tristeza.rabbitmq.producer.pool.RabbitMQTemplatePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.tristeza.rabbit.api.enums.MessageType.*;

/**
 * 发送消息
 *
 * @author chaodong.xi
 * @date 2020/7/5 2:32 下午
 */

@Component
public class ProducerClient implements MessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerClient.class);

    @Resource
    private RabbitMQTemplatePool rabbitMQTemplatePool;
    @Resource
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        switch (message.getMessageType()) {
            case CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            case RAPID:
            default:
                rabbitBroker.rapidSend(message);
        }
    }

    @Override
    public void send(Message message, SendCallback callback) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(RAPID);
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();
    }
}
