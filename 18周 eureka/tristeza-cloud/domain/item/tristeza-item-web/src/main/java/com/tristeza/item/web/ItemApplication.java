package com.tristeza.item.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chaodong.xi
 * @date 2021/2/21 7:54 下午
 */
@SpringBootApplication
@MapperScan("com.tristeza.item.mapper")
@ComponentScan(basePackages = {"com.tristeza", "com.tristeza.cloud.common.idworker"})
@EnableDiscoveryClient
@EnableHystrix
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
