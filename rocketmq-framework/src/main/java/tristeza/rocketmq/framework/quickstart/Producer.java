package tristeza.rocketmq.framework.quickstart;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author chaodong.xi
 * @date 2021/1/16 下午4:32
 */
public class Producer {
    public static final String NAME_SERVER_ADDR = "127.0.0.1:9876;127.0.0.1:9877";

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("test_topic_producer");
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setVipChannelEnabled(false);
        producer.start();


        for (int i = 0; i < 1; i++) {
            Message message = new Message("test_topic", "TagA", "HA", "Hello RocketMQ".getBytes());
            try {
                producer.send(message, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("msgId:" + sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("发送失败");
                        throwable.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        producer.shutdown();
    }
}
