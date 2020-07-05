package com.tristeza.rabbitmq.producer.client;

import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbit.api.producer.MessageProducer;
import com.tristeza.rabbit.api.producer.SendCallback;
import com.tristeza.rabbitmq.producer.async.AsyncQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发送消息
 *
 * @author chaodong.xi
 * @date 2020/7/5 2:32 下午
 */

@Component
public class RapidProducerClient implements MessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RapidProducerClient.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        AsyncQueue.submit(() -> {
                    rabbitTemplate.convertAndSend(message.getTopic(), message.getRoutingKey(),
                            message, new CorrelationData(message.getMessageId()));
                    LOGGER.info("send message to rabbitmq, messageId = {}", message.getMessageId());
                }
        );
    }

    @Override
    public void send(Message message, SendCallback callback) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages, SendCallback callback) throws MessageRunTimeException {

    }
}
