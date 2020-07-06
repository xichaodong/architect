package com.tristeza.rabbitmq.producer.db.mapper;

import com.tristeza.rabbitmq.producer.db.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BrokerMessageMapper {

    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessage record);

    int insertSelective(BrokerMessage record);

    BrokerMessage selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessage record);

    int updateByPrimaryKey(BrokerMessage record);
}