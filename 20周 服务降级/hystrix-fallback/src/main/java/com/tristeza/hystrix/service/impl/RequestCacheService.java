package com.tristeza.hystrix.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.tristeza.hystrix.service.IService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/26 9:20 下午
 */
@Service
public class RequestCacheService {
    @Resource
    private IService iService;

    @CacheResult
    @HystrixCommand(commandKey = "cacheKey")
    public String requestCache(@CacheKey String name) {
        System.err.println("send");
        String s = iService.sayHi();
        System.err.println("receive");
        return s;
    }
}
