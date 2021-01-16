package thread;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chaodong.xi
 * @date 2020/10/20 1:09 下午
 */
public class ProducerAndConsumer {
    private static final Queue<String> queue = new ArrayDeque<>();
    private static final Object LOCK = new Object();
    private static AtomicInteger COUNT = new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(new Producer(), "producer").start();
        new Thread(new Consumer(), "consumer").start();
    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (queue.isEmpty()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("消费元素 = " + queue.poll());
                    LOCK.notify();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (queue.size() == 10) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.add(String.valueOf(COUNT.incrementAndGet()));
                    LOCK.notify();
                    System.out.println("生产元素 = " + COUNT.get());
                }
            }
        }
    }
}
