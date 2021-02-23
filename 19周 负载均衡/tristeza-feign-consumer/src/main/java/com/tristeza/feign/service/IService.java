package com.tristeza.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chaodong.xi
 * @date 2021/2/23 6:54 下午
 */
@FeignClient("eureka-client")
public interface IService {
    @GetMapping("sayHi")
    String sayHi();
}
