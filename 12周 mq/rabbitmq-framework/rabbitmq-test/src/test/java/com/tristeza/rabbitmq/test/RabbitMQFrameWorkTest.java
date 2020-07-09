package com.tristeza.rabbitmq.test;

import com.tristeza.rabbit.api.builder.MessageBuilder;
import com.tristeza.rabbit.api.enums.MessageType;
import com.tristeza.rabbit.api.model.Message;
import com.tristeza.rabbitmq.producer.client.ProducerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author chaodong.xi
 * @date 2020/7/8 8:21 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RabbitMQFrameWorkTest {
    @Resource
    private ProducerClient producerClient;

    @Test
    public void send() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            String messageId = UUID.randomUUID().toString();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", "111");
            attributes.put("age", "18");
            Message message = MessageBuilder.create().withMessageId(messageId)
                    .withAttributes(attributes)
                    .withTopic("mirror-ex2")
                    .withRoutingKey("mirror.123")
                    .withMessageType(MessageType.RELIANT)
                    .withDelayMills(5000)
                    .build();
            producerClient.send(message);
        }
        Thread.sleep(100000);
    }
}
