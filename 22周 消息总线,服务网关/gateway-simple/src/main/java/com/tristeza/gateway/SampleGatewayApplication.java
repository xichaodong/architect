package com.tristeza.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chaodong.xi
 * @date 2021/3/4 4:41 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SampleGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleGatewayApplication.class, args);
    }
}
