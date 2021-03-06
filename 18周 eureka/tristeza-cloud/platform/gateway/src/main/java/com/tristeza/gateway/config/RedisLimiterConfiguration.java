package com.tristeza.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author chaodong.xi
 * @date 2021/3/5 9:02 下午
 */
@Configuration
public class RedisLimiterConfiguration {
    @Bean
    @Primary
    public KeyResolver remoteAddKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean("redisRateLimiterUser")
    @Primary
    public RedisRateLimiter redisRateLimiterUser() {
        return new RedisRateLimiter(2, 1);
    }

    @Bean("redisRateLimiterItem")
    public RedisRateLimiter redisRateLimiterItem() {
        return new RedisRateLimiter(20, 50);
    }
}
