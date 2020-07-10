package com.tristeza.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2020/7/9 6:18 下午
 */
@Component
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("kafka-topic", message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.info("发送消息失败: {}", throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                LOGGER.info("发送消息成功: {}", result.toString());
            }
        });
    }
}
