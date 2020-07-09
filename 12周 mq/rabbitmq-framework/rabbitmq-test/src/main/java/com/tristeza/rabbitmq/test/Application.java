package com.tristeza.rabbitmq.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chaodong.xi
 * @date 2020/7/5 12:08 下午
 */

@SpringBootApplication(scanBasePackages = {"com.tristeza.rabbitmq.*"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
