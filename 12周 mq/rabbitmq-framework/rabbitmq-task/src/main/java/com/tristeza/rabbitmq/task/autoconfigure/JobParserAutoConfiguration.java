package com.tristeza.rabbitmq.task.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.tristeza.rabbitmq.task.model.JobZookeeperProperties;
import com.tristeza.rabbitmq.task.parser.ElasticJobConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chaodong.xi
 * @date 2020/7/8 11:13 上午
 */
@Configuration
@ConditionalOnProperty(prefix = "elastic.job.zk", name = {"namespace", "serverLists"}, matchIfMissing = false)
@EnableConfigurationProperties(JobZookeeperProperties.class)
public class JobParserAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobZookeeperProperties.class);

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(JobZookeeperProperties jobZookeeperProperties) {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(jobZookeeperProperties.getServerLists(),
                jobZookeeperProperties.getNamespace());
        zkConfig.setConnectionTimeoutMilliseconds(jobZookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setMaxRetries(jobZookeeperProperties.getMaxRetries());
        zkConfig.setBaseSleepTimeMilliseconds(jobZookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setMaxSleepTimeMilliseconds(jobZookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(jobZookeeperProperties.getSessionTimeoutMilliseconds());
        zkConfig.setDigest(jobZookeeperProperties.getDigest());

        LOGGER.info("ZooKeeper配置成功,namespace:{}, serverList:{}", zkConfig.getNamespace(), zkConfig.getServerLists());

        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    public ElasticJobConfigParser elasticJobConfigParser(JobZookeeperProperties jobZookeeperProperties,
                                                         ZookeeperRegistryCenter zookeeperRegistryCenter) {
        return new ElasticJobConfigParser(jobZookeeperProperties, zookeeperRegistryCenter);
    }

}
