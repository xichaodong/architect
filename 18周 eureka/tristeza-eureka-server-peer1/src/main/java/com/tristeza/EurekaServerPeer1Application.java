package com.tristeza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author chaodong.xi
 * @date 2020/8/13 3:34 下午
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerPeer1Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerPeer1Application.class, args);
    }
}
