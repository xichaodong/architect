package com.tristeza.auth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:05 下午
 */
@FeignClient(value = "auth-api")
public interface AuthApi {
    @PostMapping("login")
    String login(@RequestParam("username") String username, @RequestParam("password") String password);

    @GetMapping("verify")
    String verify(@RequestParam("username") String username, @RequestParam("token") String token);

    @GetMapping("refresh")
    String refresh(@RequestParam("refreshToken") String refreshToken);
}