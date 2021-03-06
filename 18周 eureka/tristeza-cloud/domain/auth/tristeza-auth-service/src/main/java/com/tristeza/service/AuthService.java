package com.tristeza.service;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:15 下午
 */
public interface AuthService {
    String login(String username, String password);

    String verify(String username, String token);

    String refresh(String refreshToken, String token);
}
