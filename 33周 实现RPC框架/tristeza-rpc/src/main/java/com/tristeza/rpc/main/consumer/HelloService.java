package com.tristeza.rpc.main.consumer;

/**
 * @author chaodong.xi
 * @date 2021/2/12 10:15 下午
 */
public interface HelloService {
    String hello(String name);

    String hello(User user);
}
