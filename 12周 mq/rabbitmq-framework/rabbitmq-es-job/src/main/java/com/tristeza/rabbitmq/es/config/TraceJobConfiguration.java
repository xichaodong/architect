package com.tristeza.rabbitmq.es.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tristeza.rabbitmq.es.annotation.JobTraceInterceptor;

//@Configuration
public class TraceJobConfiguration {

    @Bean
    public JobTraceInterceptor jobTraceInterceptor() {
        System.err.println("init --------------->");
        return new JobTraceInterceptor();
    }

}
