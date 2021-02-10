package com.tristeza.rpc.async;

/**
 * @author chaodong.xi
 * @date 2021/2/10 1:17 下午
 */
public interface RpcCallback {
    void success(Object result);

    void failure(Throwable cause);
}
