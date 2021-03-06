package com.tristeza.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author chaodong.xi
 * @date 2021/3/5 2:40 下午
 */
@Component
public class TimeFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(exchange.getRequest().getURI().getRawPath());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            stopWatch.stop();
            System.err.println(stopWatch.prettyPrint());
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
