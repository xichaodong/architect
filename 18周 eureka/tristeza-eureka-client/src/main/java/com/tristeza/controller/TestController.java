package com.tristeza.controller;

import com.tristeza.model.Friend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chaodong.xi
 * @date 2020/8/15 3:06 下午
 */
@RestController
public class TestController {
    @GetMapping("sayHi")
    public String hello(HttpServletRequest request) {
        System.out.println("say hi");
        return request.getLocalPort() + "";
    }

    @GetMapping("timeout")
    public String timeout() throws InterruptedException {
        Thread.sleep(500);
        System.err.println("receive");
        return "success";
    }

    @GetMapping("error")
    public String error() {
        throw new RuntimeException("error");
    }

    @PostMapping("/sayHi")
    public String helloPost(@RequestBody Friend friend) {
        return friend.getName();
    }
}
