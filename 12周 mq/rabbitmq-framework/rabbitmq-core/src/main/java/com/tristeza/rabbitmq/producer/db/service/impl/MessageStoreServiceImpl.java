package com.tristeza.rabbitmq.producer.db.service.impl;

import com.tristeza.rabbitmq.producer.db.entity.BrokerMessage;
import com.tristeza.rabbitmq.producer.db.mapper.BrokerMessageMapper;
import com.tristeza.rabbitmq.producer.db.service.MessageStoreService;
import com.tristeza.rabbitmq.producer.enums.BrokerMessageStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author chaodong.xi
 * @date 2020/7/7 3:47 下午
 */
@Service
public class MessageStoreServiceImpl implements MessageStoreService {
    @Resource
    private BrokerMessageMapper brokerMessageMapper;

    @Override
    public int insert(BrokerMessage message) {
        return brokerMessageMapper.insert(message);
    }

    @Override
    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    @Override
    public void failure(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }
}
