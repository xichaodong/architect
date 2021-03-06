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

    Account{username='qw', token='eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0cmlzdGV6YSIsImV4cCI6MTYxNDk0MjU1MCwiaWF0IjoxNjE0OTQyNDkwLCJ1c2VybmFtZSI6InF3In0.jknkRXDsJnfDmpJNCpfm1f4pOSMlDwujqMr3N6u2rXs', refreshToken='ae46976d-8d2e-452a-8544-0bb0c787807d'}
