package com.tristeza.kafka.consumer;

import com.tristeza.kafka.producer.KafkaProducer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author chaodong.xi
 * @date 2020/7/9 6:20 下午
 */
@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(groupId = " kafka-group", topics = "kafka-topic")
    public void onMessage(ConsumerRecord<String, Object> records,
                          Acknowledgment acknowledgment,
                          Consumer<?, ?> consumer) {
        LOGGER.info("消费端消费消息:{}", records.value());
        acknowledgment.acknowledge();
    }
}
