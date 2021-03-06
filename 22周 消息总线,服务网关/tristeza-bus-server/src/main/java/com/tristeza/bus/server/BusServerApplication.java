package com.tristeza.bus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author chaodong.xi
 * @date 2021/3/4 12:13 下午
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class BusServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusServerApplication.class, args);
    }
}
