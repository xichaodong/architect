package com.tristeza.rabbitmq.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//import com.bfxy.rabbit.task.annotation.EnableElasticJob;
//
//@EnableElasticJob
@SpringBootApplication
@ComponentScan(basePackages = {"com.tristeza.rabbitmq.es.*",
		"com.tristeza.rabbitmq.es.service.*",
		"com.tristeza.rabbitmq.es.annotation",
		"com.tristeza.rabbitmq.es.task"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
