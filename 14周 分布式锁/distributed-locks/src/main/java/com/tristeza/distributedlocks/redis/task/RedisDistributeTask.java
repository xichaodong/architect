package com.tristeza.distributedlocks.redis.task;

import com.tristeza.distributedlocks.redis.utils.RedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/7/16 5:36 下午
 */
@Service
public class RedisDistributeTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributeTask.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    //    @Scheduled(cron = "0/5 * * * * ?")
    public void commonTask() {
        try (RedisLock lock = RedisLock.create()
                .stringRedisTemplate(stringRedisTemplate)
                .key("redisKey")
                .expireTime(30)
                .build()) {
            if (lock.getLock()) {
                LOGGER.info("执行定时任务");
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void redissonTask() throws InterruptedException {
        LOGGER.info("进入方法");

        RLock lock = redissonClient.getLock("order");
        lock.lockInterruptibly(30, TimeUnit.SECONDS);

        LOGGER.info("获得锁");

        TimeUnit.SECONDS.sleep(5);

        LOGGER.info("方法执行完毕");
    }
}
