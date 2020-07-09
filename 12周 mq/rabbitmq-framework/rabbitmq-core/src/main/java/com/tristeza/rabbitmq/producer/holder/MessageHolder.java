package com.tristeza.rabbitmq.producer.holder;

import com.tristeza.rabbit.api.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/7/9 12:19 上午
 */
public class MessageHolder {
    private static final ThreadLocal<MessageHolder> holder = new ThreadLocal<>();

    private List<Message> messages = new ArrayList<>();

    public static void add(Message message) {
        holder.get().messages.add(message);
    }

    public static List<Message> clear() {
        List<Message> messages = holder.get().messages;
        holder.remove();

        return messages;
    }
}
