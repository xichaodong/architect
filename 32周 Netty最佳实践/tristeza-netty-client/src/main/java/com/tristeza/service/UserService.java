package com.tristeza.service;

import com.tristeza.protobuf.MessageModule;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午12:53
 */
public interface UserService {
    void save(MessageModule.ResultType resultType, byte[] data);

    void update(MessageModule.ResultType resultType, byte[] data);
}
