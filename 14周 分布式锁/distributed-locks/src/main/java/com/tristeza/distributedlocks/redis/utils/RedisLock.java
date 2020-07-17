package com.tristeza.distributedlocks.redis.utils;

import com.tristeza.distributedlocks.redis.controller.RedisLockController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Collections;
import java.util.UUID;

/**
 * @author chaodong.xi
 * @date 2020/7/16 4:41 下午
 */
public class RedisLock implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockController.class);

    private StringRedisTemplate stringRedisTemplate;
    private String key;
    private String value;
    private int expireTime;


    public RedisLock stringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        return this;
    }

    public static RedisLock create() {
        return new RedisLock();
    }

    public RedisLock key(String key) {
        this.key = key;
        return this;
    }

    public RedisLock expireTime(int expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public RedisLock build() {
        this.value = UUID.randomUUID().toString();
        return this;
    }

    public boolean getLock() {
        RedisCallback<Boolean> redisCallback = redisConnection -> {
            RedisCommands.SetOption setOption = RedisCommands.SetOption.ifAbsent();
            Expiration expiration = Expiration.seconds(expireTime);

            byte[] redisKey = stringRedisTemplate.getStringSerializer().serialize(key);
            byte[] redisValue = stringRedisTemplate.getStringSerializer().serialize(value);

            assert redisKey != null;
            assert redisValue != null;

            return redisConnection.set(redisKey, redisValue, expiration, setOption);
        };

        return stringRedisTemplate.execute(redisCallback);
    }

    public boolean unlock() {
        RedisScript<Boolean> script = RedisScript.of(
                "if redis.call('get',KEYS[1]) == ARGV[1] then\n" +
                        "return redis.call('del',KEYS[1])\n" +
                        "else\n" +
                        "return 0\n" +
                        "end", Boolean.class
        );

        return stringRedisTemplate.execute(script, Collections.singletonList(key), value);
    }

    @Override
    public void close() throws Exception {
        boolean unlock = unlock();
//        LOGGER.info("释放锁的结果: {}", unlock);
    }
}
