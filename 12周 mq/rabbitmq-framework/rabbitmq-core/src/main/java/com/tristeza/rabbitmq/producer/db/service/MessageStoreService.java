package com.tristeza.rabbitmq.producer.db.service;

import com.tristeza.rabbitmq.producer.db.entity.BrokerMessage;
import com.tristeza.rabbitmq.producer.enums.BrokerMessageStatus;

import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/7/7 3:47 下午
 */
public interface MessageStoreService {
    int insert(BrokerMessage message);

    void success(String messageId);

    void failure(String messageId);

    List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus);

    int updateTryCount(String brokerMessageId);

    BrokerMessage selectByMessageId(String messageId);
}
