package com.tristeza.rabbitmq.producer.config;

import com.tristeza.rabbitmq.task.annotation.EnableElasticJob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 *
 * @author chaodong.xi
 * @date 2020/7/5 2:17 下午
 */
@EnableElasticJob
@ComponentScan({"com.tristeza.rabbitmq.producer.*"})
@Configuration
public class RabbitMQProducerAutoConfiguration {
}
