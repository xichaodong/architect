package com.tristeza.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chaodong.xi
 * @date 2020/8/31 4:05 下午
 */
@Configuration
public class RibbonConfig2 {
    @Bean
    public IRule myRule2() {
        return new RandomRule();
    }
}
