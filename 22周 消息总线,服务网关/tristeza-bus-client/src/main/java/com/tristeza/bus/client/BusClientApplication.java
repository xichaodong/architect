package com.tristeza.bus.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chaodong.xi
 * @date 2021/3/4 12:13 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BusClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusClientApplication.class, args);
    }
}
