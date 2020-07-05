package com.chloe.test;

import com.chloe.mq.Application;
import com.chloe.mq.Receiver;
import com.chloe.mq.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MqTest {
    @Resource
    private Sender sender;
    @Resource
    private Receiver receiver;

    @Test
    public void send() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("attr1", "12345");
        properties.put("attr2", "45678");

        for (int i = 0; i < 100; i++)
            sender.send("hello rabbitmq", properties);
    }
}
