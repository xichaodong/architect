package thread;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chaodong.xi
 * @date 2020/10/20 1:09 下午
 */
public class ProducerAndConsumerLinked {
    private static final LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>(10);
    private static final Object LOCK = new Object();
    private static volatile AtomicInteger COUNT = new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(new Producer(), "producer").start();
        new Thread(new Consumer(), "consumer").start();
    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            try {
                for (; ; )
                    System.out.println("消费元素 = " + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                try {
                    queue.put(String.valueOf(COUNT.incrementAndGet()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("生产元素 = " + COUNT.get());
            }
        }
    }
}
