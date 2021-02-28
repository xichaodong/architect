package com.tristeza.hystrix.service;

import com.tristeza.hystrix.fallback.IServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chaodong.xi
 * @date 2021/2/23 6:54 下午
 */
@FeignClient(value = "eureka-client", fallback = IServiceFallback.class)
public interface IService {
    @GetMapping("sayHi")
    String sayHi();

    @GetMapping("error")
    String error();

    @GetMapping("timeout")
    String timeout();
}
