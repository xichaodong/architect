package com.tristeza.gateway.config;

import com.tristeza.gateway.filter.TimeFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2021/3/5 11:59 上午
 */
@Configuration
public class GatewayConfiguration {
    @Resource
    private TimeFilter timeFilter;

    @Bean
    @Order
    public RouteLocator customizedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/java/**")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(f -> f.stripPrefix(1)
                                .addResponseHeader("java-param", "gateway-config")
                                .filter(timeFilter))
                        .uri("lb://CONFIG-BUS-SERVER"))
                .build();
    }
}
