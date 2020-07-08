package com.tristeza.rabbitmq.task.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author chaodong.xi
 * @date 2020/7/8 11:13 上午
 */
@Configuration
@ConditionalOnProperty(prefix = "elastic.job.zk", name = {"namespace", "serverLists"}, matchIfMissing = false)
public class JobParserAutoConfiguration {
}
