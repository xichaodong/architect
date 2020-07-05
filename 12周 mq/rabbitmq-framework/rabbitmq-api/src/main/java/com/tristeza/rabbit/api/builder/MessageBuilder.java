package com.tristeza.rabbit.api.builder;

import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author chaodong.xi
 * @date 2020/7/5 1:08 下午
 */

public class MessageBuilder {
    private String messageId;

    private String topic;

    private String routingKey = "";

    private Map<String, Object> attributes = new HashMap<>();

    private int delayMills;

    private int messageType = MessageType.CONFIRM;

    private MessageBuilder() {
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(int messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message build() {
        if (StringUtils.isEmpty(messageId)) {
            messageId = UUID.randomUUID().toString();
        }
        if (StringUtils.isEmpty(topic)) {
            throw new MessageRunTimeException("topic is null");
        }
        return new Message(messageId, topic, routingKey, attributes, delayMills, messageType);
    }
}
