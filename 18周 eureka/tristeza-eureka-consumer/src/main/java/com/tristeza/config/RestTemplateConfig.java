package com.tristeza.config;

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
    public RestTemplate register() {
        return new RestTemplate();
    }
}
