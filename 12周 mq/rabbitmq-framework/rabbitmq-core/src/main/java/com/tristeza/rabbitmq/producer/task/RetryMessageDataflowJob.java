package com.tristeza.rabbitmq.producer.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.tristeza.rabbitmq.producer.broker.RabbitBroker;
import com.tristeza.rabbitmq.producer.db.entity.BrokerMessage;
import com.tristeza.rabbitmq.producer.db.service.MessageStoreService;
import com.tristeza.rabbitmq.producer.enums.BrokerMessageStatus;
import com.tristeza.rabbitmq.task.annotation.ElasticJobConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/7/8 6:58 下午
 */
@Component
@ElasticJobConfig(name = "RetryMessageDataflowJob", cron = "0/10 * * * * ?",
        description = "可靠性消息补偿任务", overwrite = true, shardingTotalCount = 1)
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {
    private final Logger LOGGER = LoggerFactory.getLogger(RetryMessageDataflowJob.class);
    private static final int MAX_RETRY_COUNT = 3;

    @Resource
    private MessageStoreService messageStoreService;
    @Resource
    private RabbitBroker rabbitBroker;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> messages = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);

        LOGGER.info("=====抓取数据集合=====，数量：" + messages.size());

        return messages;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> data) {
        data.forEach(message -> {
            if (message.getTryCount() > MAX_RETRY_COUNT) {
                messageStoreService.failure(message.getMessageId());
                LOGGER.info("=====消息发送重试超过最大次数=====，消息ID：" + message.getMessageId());
            } else {
                messageStoreService.updateTryCount(message.getMessageId());
                rabbitBroker.reliantSend(message.getMessage());
            }
        });
    }
}
