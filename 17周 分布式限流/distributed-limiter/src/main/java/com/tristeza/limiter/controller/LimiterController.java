package com.tristeza.limiter.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/8/5 11:40 下午
 */
@RestController
public class LimiterController {
    private static final RateLimiter rateLimiter = RateLimiter.create(2.0);
    private static final Logger LOGGER = LoggerFactory.getLogger(LimiterController.class);

    @GetMapping("tryAcquire")
    public String tryAcquire(@RequestParam Integer count) {
        if (rateLimiter.tryAcquire(count)) {
            LOGGER.info("请求成功，限速={}", rateLimiter.getRate());
            return "success";
        } else {
            LOGGER.info("请求失败，限速={}", rateLimiter.getRate());
            return "failed";
        }
    }

    @GetMapping("tryAcquireWithTimeout")
    public String tryAcquire(@RequestParam Integer count, @RequestParam Integer timeout) {
        if (rateLimiter.tryAcquire(count, timeout, TimeUnit.SECONDS)) {
            LOGGER.info("请求成功，限速={}", rateLimiter.getRate());
            return "success";
        } else {
            LOGGER.info("请求失败，限速={}", rateLimiter.getRate());
            return "failed";
        }
    }
}
