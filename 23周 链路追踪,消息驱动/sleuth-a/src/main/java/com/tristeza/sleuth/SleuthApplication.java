package com.tristeza.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chaodong.xi
 * @date 2021/3/6 9:14 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SleuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }
}
