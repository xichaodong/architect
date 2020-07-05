package com.tristeza.rabbitmq.producer.async;

import com.tristeza.rabbitmq.producer.client.RapidProducerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 异步队列
 *
 * @author chaodong.xi
 * @date 2020/7/5 4:32 下午
 */
public class AsyncQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncQueue.class);

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int QUEUE_SIZE = 10000;

    private static ExecutorService senderAsync = new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE), r -> {
        Thread thread = new Thread(r);
        thread.setName("rabbitmq_client_async_sender");
        return thread;
    }, (r, executor) -> LOGGER.error("async sender was reject, runnable={}, executor={}", r, executor));

    public static void submit(Runnable runnable) {
        senderAsync.submit(runnable);
    }
}
