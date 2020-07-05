package com.tristeza.rabbitmq.producer.client;

import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbit.api.producer.MessageProducer;
import com.tristeza.rabbit.api.producer.SendCallback;

import java.util.List;

/**
 * 发送消息
 *
 * @author chaodong.xi
 * @date 2020/7/5 2:32 下午
 */
public class ConfirmProducerClient implements MessageProducer {
    @Override
    public void send(Message message) throws MessageRunTimeException {

    }

    @Override
    public void send(Message message, SendCallback callback) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages, SendCallback callback) throws MessageRunTimeException {

    }
}
