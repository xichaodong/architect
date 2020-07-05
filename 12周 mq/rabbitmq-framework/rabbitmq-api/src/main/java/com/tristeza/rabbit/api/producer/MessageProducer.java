package com.tristeza.rabbit.api.producer;

import com.tristeza.rabbit.api.exception.MessageRunTimeException;
import com.tristeza.rabbit.api.model.Message;

import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/7/5 1:56 下午
 */
public interface MessageProducer {
    /**
     * 消息发送，无回调
     *
     * @param message 消息
     * @throws MessageRunTimeException 异常
     */
    void send(Message message) throws MessageRunTimeException;

    /**
     * 单条消息发送，有回调
     *
     * @param message  消息
     * @param callback 回调
     * @throws MessageRunTimeException 异常
     */
    void send(Message message, SendCallback callback) throws MessageRunTimeException;

    /**
     * 批量消息发送，有回调
     *
     * @param messages 消息
     * @param callback 回调
     * @throws MessageRunTimeException 异常
     */
    void send(List<Message> messages, SendCallback callback) throws MessageRunTimeException;
}
