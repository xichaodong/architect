package com.tristeza.kafka.test;

import com.tristeza.kafka.Application;
import com.tristeza.kafka.consumer.KafkaConsumer;
import com.tristeza.kafka.producer.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2020/7/9 6:23 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class KafkaTest {
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private KafkaConsumer kafkaConsumer;

    @Test
    public void send() {
        for (int i = 0; i < 100; i++) {
            kafkaProducer.send("hello, kafka " + i);
        }
    }
}
