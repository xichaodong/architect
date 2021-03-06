package com.tristeza.auth.web;

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
@ComponentScan(basePackages = {"com.tristeza"})
@EnableDiscoveryClient
@EnableHystrix
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
