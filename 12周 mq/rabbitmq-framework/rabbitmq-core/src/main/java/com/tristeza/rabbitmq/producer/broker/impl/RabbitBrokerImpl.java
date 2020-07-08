package com.tristeza.rabbitmq.producer.broker.impl;

import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbitmq.producer.async.AsyncQueue;
import com.tristeza.rabbitmq.producer.broker.RabbitBroker;
import com.tristeza.rabbitmq.producer.constant.BrokerMessageConst;
import com.tristeza.rabbitmq.producer.db.entity.BrokerMessage;
import com.tristeza.rabbitmq.producer.db.service.MessageStoreService;
import com.tristeza.rabbitmq.producer.enums.BrokerMessageStatus;
import com.tristeza.rabbitmq.producer.pool.RabbitMQTemplatePool;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author chaodong.xi
 * @date 2020/7/6 8:36 下午
 */
@Component
public class RabbitBrokerImpl implements RabbitBroker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitBrokerImpl.class);

    @Resource
    private RabbitMQTemplatePool rabbitMQTemplatePool;

    @Resource
    private MessageStoreService messageStoreService;

    @Override
    public void rapidSend(Message message) {
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);

        Date now = new Date();
        BrokerMessage brokerMessage = new BrokerMessage();

        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
        brokerMessage.setMessage(message);
        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);

        messageStoreService.insert(brokerMessage);
    }

    private void sendKernel(Message message) {
        AsyncQueue.submit(() -> {
                    rabbitMQTemplatePool.getTemplate(message)
                            .convertAndSend(message.getTopic(), message.getRoutingKey(),
                                    message, new CorrelationData(String.format("%s#%s", message.getMessageId(),
                                            System.currentTimeMillis())));
                    LOGGER.info("send message to rabbitmq, messageId = {}", message.getMessageId());
                }
        );
    }
}
