package com.tristeza.service;

import com.tristeza.model.Response;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午12:53
 */
public interface UserService {
    Response<?> save(byte[] data);

    Response<?> update(byte[] data);
}
