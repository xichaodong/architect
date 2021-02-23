package com.tristeza.feign.controller;

import com.tristeza.feign.service.IService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/23 6:56 下午
 */
@RestController
public class TestController {
    @Resource
    private IService iService;

    @GetMapping("sayHi")
    public String sayHi() {
        return iService.sayHi();
    }
}
