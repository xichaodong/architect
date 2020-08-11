package com.tristeza.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.tristeza.annotation.AccessLimiter;
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

    @GetMapping("acquire")
    public String acquire(@RequestParam Integer count) {
        rateLimiter.acquire(count);
        LOGGER.info("请求成功，限速={}", rateLimiter.getRate());
        return "success";
    }

    @AccessLimiter(limit = 1)
    @GetMapping("nginx")
    public String nginx() throws InterruptedException {
        return "success";
    }

    public static void main(String[] args) throws NoSuchMethodException {
        /* 设置此系统属性,让JVM生成的Proxy类写入文件.保存路径为：com/sun/proxy(如果不存在请手工创建) */
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        AccessLimiter annotation = LimiterController.class.getMethod("nginx").getAnnotation(AccessLimiter.class);//获取TestMain类上的注解对象
        System.out.println(annotation.limit());//调用注解对象的say方法，并打印到控制台
    }
}
