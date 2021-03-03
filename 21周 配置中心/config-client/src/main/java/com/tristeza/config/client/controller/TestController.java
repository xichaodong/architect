package com.tristeza.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaodong.xi
 * @date 2021/3/3 12:11 下午
 */
@RestController
@RefreshScope
public class TestController {
    @Value("${name}")
    private String name;
    @Value("${myWords}")
    private String words;

    @GetMapping("test")
    public String getName() {
        return name;
    }

    @GetMapping("words")
    public String getWords() {
        return words;
    }
}
