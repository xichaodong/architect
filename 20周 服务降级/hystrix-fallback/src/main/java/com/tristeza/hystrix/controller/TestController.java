package com.tristeza.hystrix.controller;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.tristeza.hystrix.service.IService;
import com.tristeza.hystrix.service.impl.RequestCacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/23 6:56 下午
 */
@RestController
public class TestController {
    @Resource
    private RequestCacheService requestCacheService;

    @GetMapping("sayHi")
    public String sayHi(@RequestParam String name) {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();

        String data;
        try {
            data = requestCacheService.requestCache(name);
            data = requestCacheService.requestCache(name);
        } finally {
            context.close();
        }

        return data;
    }
}
