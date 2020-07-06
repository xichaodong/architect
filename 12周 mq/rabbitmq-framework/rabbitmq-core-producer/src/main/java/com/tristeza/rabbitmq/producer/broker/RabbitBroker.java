package com.tristeza.rabbitmq.producer.broker;

import com.tristeza.rabbit.api.model.Message;

/**
 * @author chaodong.xi
 * @date 2020/7/6 8:34 下午
 */
public interface RabbitBroker {
    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliantSend(Message message);
}
