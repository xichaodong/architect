package com.tristeza.rpc.main.consumer;

/**
 * @author chaodong.xi
 * @date 2021/2/12 10:18 下午
 */
public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
