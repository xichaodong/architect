package com.chloe.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "mirror-queue", durable = "true"),
            exchange = @Exchange(name = "mirror-ex", durable = "true",
                    type = "topic", ignoreDeclarationExceptions = "true"),
            key = "mirror.*"
    )
    )
    @RabbitHandler
    public void onMessage(Message<?> message, Channel channel) throws Exception {
        System.out.println("---------------");
        System.out.println("消费消息: " + message.getPayload());

        long deliveryTag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
//        channel.basicAck(deliveryTag, false);
    }
}
