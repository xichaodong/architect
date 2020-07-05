package com.tristeza.rabbit.api.model;

import com.tristeza.rabbit.api.enums.MessageType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chaodong.xi
 * @date 2020/7/5 12:44 下午
 */

public class Message implements Serializable {

    private static final long serialVersionUID = -1893208487389778692L;

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息路由规则
     */
    private String routingKey = "";

    /**
     * 消息属性
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 消息延迟时间
     */
    private int delayMills;

    /**
     * 消息类型
     */
    private int messageType = MessageType.CONFIRM;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey,
                   Map<String, Object> attributes, int delayMills, int messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public int getDelayMills() {
        return delayMills;
    }

    public void setDelayMills(int delayMills) {
        this.delayMills = delayMills;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
