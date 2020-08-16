package com.tristeza.controller;

import com.tristeza.model.Friend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaodong.xi
 * @date 2020/8/15 3:06 下午
 */
@RestController
public class TestController {
    @GetMapping("/sayHi")
    public String hello() {
        System.out.println("say hi");
        return "success";
    }

    @PostMapping("/sayHi")
    public String helloPost(@RequestBody Friend friend) {
        return friend.getName();
    }
}
