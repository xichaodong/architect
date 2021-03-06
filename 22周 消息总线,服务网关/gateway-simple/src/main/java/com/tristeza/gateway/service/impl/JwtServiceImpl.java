package com.tristeza.gateway.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tristeza.gateway.model.Account;
import com.tristeza.gateway.service.JwtService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:18 下午
 */
@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String token(Account account) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256("tristeza");
        return JWT.create().withIssuer("tristeza")
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + 60 * 1000))
                .withClaim("username", account.getUsername())
                .sign(algorithm);
    }

    @Override
    public boolean verify(String username, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("tristeza");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("tristeza")
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
