package com.tristeza.sleuth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/3/6 9:22 下午
 */
@RestController
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("test")
    public String test() {
        LOGGER.info("-------Trace A");

        return restTemplate.getForEntity("http://sleuth-b/test", String.class).getBody();
    }
}
