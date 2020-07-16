package com.tristeza.distributedlocks;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tristeza.distributedlocks.mysql.mapper")
public class DistributedLocksApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLocksApplication.class, args);
    }

}
