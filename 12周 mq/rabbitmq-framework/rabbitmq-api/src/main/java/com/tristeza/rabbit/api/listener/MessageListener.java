package com.tristeza.rabbit.api.listener;

import com.tristeza.rabbit.api.model.Message;

/**
 * @author chaodong.xi
 * @date 2020/7/5 2:02 下午
 */
public interface MessageListener {
    void onMessage(Message message);
}
