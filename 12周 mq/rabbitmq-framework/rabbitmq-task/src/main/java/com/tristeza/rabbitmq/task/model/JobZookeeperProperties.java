package com.tristeza.rabbitmq.task.model;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chaodong.xi
 * @date 2020/7/8 11:40 上午
 */
@Component
@ConfigurationProperties(prefix = "elastic.job.zk")
public class JobZookeeperProperties {
}
