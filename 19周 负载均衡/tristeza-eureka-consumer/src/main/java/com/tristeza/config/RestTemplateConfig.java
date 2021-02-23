package com.tristeza.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author chaodong.xi
 * @date 2020/8/15 3:14 下午
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate register() {
        return new RestTemplate();
    }

    @Bean
    public IRule defaultLBStrategy() {
        return new HashRule();
    }
}
