package com.tristeza.rabbitmq.es.service;

import org.springframework.stereotype.Service;

import com.tristeza.rabbitmq.es.annotation.JobTrace;

@Service
public class IndexService {

    @JobTrace
    public void tester(String name) {
        System.err.println("name: " + name);
    }
}
