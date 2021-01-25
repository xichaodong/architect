package tristeza.rocketmq.framework.quickstart;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author chaodong.xi
 * @date 2021/1/16 下午4:32
 */
public class Consumer {
    public static final String NAME_SERVER_ADDR = "127.0.0.1:9876;127.0.0.1:9877";

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_topic_consumer_name");
        consumer.setNamesrvAddr(NAME_SERVER_ADDR);
        consumer.setVipChannelEnabled(false);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe("test_topic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            MessageExt msg = list.get(0);
            try {
                String topic = msg.getTopic();
                String tags = msg.getTags();
                String keys = msg.getKeys();
                String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                int reconsumeTimes = msg.getReconsumeTimes();
                System.out.printf("topic:%s,tags:%s,keys:%s,body:%s\n", topic, tags, keys, body);
            } catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("消费端启动...");
    }
}
