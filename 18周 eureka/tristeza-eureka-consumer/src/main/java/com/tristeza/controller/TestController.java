package com.tristeza.controller;

import com.tristeza.model.Friend;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/8/15 3:06 下午
 */
@RestController
public class TestController {
    @Resource
    private LoadBalancerClient loadBalancerClient;
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        ServiceInstance instance = loadBalancerClient.choose("eureka-client");

        if (Objects.isNull(instance)) {
            return "no available instances";
        }

        String target = String.format("http://%s:%s/sayHi", instance.getHost(), instance.getPort());
        System.out.println("url is:" + target);

        return restTemplate.getForObject(target, String.class);
    }

    @PostMapping("/hello")
    public String helloPost() {
        ServiceInstance instance = loadBalancerClient.choose("eureka-client");

        if (Objects.isNull(instance)) {
            return null;
        }

        String target = String.format("http://%s:%s/sayHi", instance.getHost(), instance.getPort());
        System.out.println("url is:" + target);

        Friend friend = new Friend();
        friend.setName("eureka consumer friend");

        return restTemplate.postForObject(target, friend, String.class);
    }
}
