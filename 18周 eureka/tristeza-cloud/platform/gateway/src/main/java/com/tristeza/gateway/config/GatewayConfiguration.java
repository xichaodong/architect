package com.tristeza.gateway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/3/5 8:51 下午
 */
@Configuration
public class GatewayConfiguration {
    @Resource
    private KeyResolver keyResolver;
    @Resource
    @Qualifier("redisRateLimiterUser")
    private RateLimiter<RedisRateLimiter.Config> rateLimiter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/address/**", "/passport/**", "/userInfo/**", "/center/**")
                        .filters(f -> f.requestRateLimiter(request -> {
                            request.setKeyResolver(keyResolver);
                            request.setRateLimiter(rateLimiter);
                            request.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://TRISTEZA-CLOUD-USER"))
                .route(r -> r.path("/items/**", "/mycomments/**")
                        .uri("lb://TRISTEZA-CLOUD-ITEM"))
                .route(r -> r.path("/shopCart/**")
                        .uri("lb://TRISTEZA-CLOUD-CART"))
                .route(r -> r.path("/myorders/**", "/mycomments/**", "/orders/**")
                        .uri("lb://TRISTEZA-CLOUD-ORDER"))
                .build();
    }
}
