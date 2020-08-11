package com.tristeza.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/8/9 7:34 下午
 */
@Component
public class AccessLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLimiter.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisScript<Boolean> rateLimitLua;

    public void limitAccess(String key, Integer limit) {
        Boolean acquired = stringRedisTemplate.execute(rateLimitLua, Lists.newArrayList(key), limit.toString());

        if (Objects.nonNull(acquired) && !acquired) {
            LOGGER.error("请求被阻止");
            throw new RuntimeException("请求被阻止");
        }
    }
}
