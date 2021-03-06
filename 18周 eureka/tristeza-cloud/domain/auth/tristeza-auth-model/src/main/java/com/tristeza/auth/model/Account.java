package com.tristeza.auth.model;

import java.io.Serializable;

/**
 * @author chaodong.xi
 * @date 2021/3/5 6:03 下午
 */
public class Account implements Serializable {
    private String username;

    private String token;

    private String refreshToken;

    public Account() {
    }

    public Account(String username, String token, String refreshToken) {
        this.username = username;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
