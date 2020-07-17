package com.tristeza.distributedlocks.redis.task;

import com.tristeza.distributedlocks.redis.utils.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
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

//    @Scheduled(cron = "0/5 * * * * ?")
    public void task() {
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
}
