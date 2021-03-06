package com.tristeza.auth.remote;

import com.tristeza.auth.api.AuthApi;
import com.tristeza.auth.model.Account;
import com.tristeza.service.AuthService;
import com.tristeza.service.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:13 下午
 */
@RestController
public class AuthRemoteController implements AuthApi {
    @Resource
    private AuthService authService;
    @Resource
    private JwtService jwtService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String login(String username, String password) {
        Account account = new Account();

        account.setUsername(username);

        String token = jwtService.token(account);

        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return account.toString();
    }

    @Override
    public String verify(String username, String token) {
        return jwtService.verify(username, token) ? "success" : "token";
    }

    @Override
    public String refresh(String refreshToken) {
        Object object = redisTemplate.opsForValue().get(refreshToken);

        if (Objects.isNull(object)) {
            return "";
        }

        Account account = (Account) object;
        String token = jwtService.token(account);

        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(account.getRefreshToken(), account);

        return account.toString();
    }
}
