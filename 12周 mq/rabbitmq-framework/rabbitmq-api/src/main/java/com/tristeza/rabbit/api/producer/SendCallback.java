package com.tristeza.rabbit.api.producer;

/**
 * @author chaodong.xi
 * @date 2020/7/5 1:58 下午
 */
public interface SendCallback {
    void onSuccess();

    void onFailure();
}
