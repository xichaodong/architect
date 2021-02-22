package com.tristeza.order.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chaodong.xi
 * @date 2021/2/22 3:24 下午
 */
@SpringBootApplication
@MapperScan("com.tristeza.order.mapper")
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.tristeza", "com.tristeza.cloud.common.idworker"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
