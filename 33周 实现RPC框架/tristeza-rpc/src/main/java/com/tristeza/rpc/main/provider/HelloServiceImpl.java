package com.tristeza.rpc.main.provider;

import com.tristeza.rpc.main.consumer.HelloService;
import com.tristeza.rpc.main.consumer.User;

/**
 * @author chaodong.xi
 * @date 2021/2/12 10:17 下午
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return String.format("hello! %s", name);
    }

    @Override
    public String hello(User user) {
        return String.format("hello! %s", user.getName());
    }
}
