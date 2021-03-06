package com.tristeza.gateway;

import com.tristeza.auth.api.AuthApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author chaodong.xi
 * @date 2021/2/28 3:06 下午
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackageClasses = AuthApi.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
