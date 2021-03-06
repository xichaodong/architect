package com.tristeza.auth.service.impl;

import com.tristeza.auth.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:16 下午
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String login(String username, String password) {
        return null;
    }

    @Override
    public String verify(String username, String token) {
        return null;
    }

    @Override
    public String refresh(String refreshToken, String token) {
        return null;
    }
}
