package com.tristeza.auth.service;

import com.tristeza.auth.model.Account;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:17 下午
 */
public interface JwtService {
    String token(Users users);

    boolean verify(String username, String token);
}
