package com.tristeza.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author chaodong.xi
 * @date 2020/8/13 3:34 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableTurbine
public class HystrixTurbineApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixTurbineApplication.class, args);
    }
}
