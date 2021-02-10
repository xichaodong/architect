package com.tristeza.rpc.proxy;

import com.tristeza.rpc.async.RpcFuture;

/**
 * @author chaodong.xi
 * @date 2021/2/11 12:22 上午
 */
public interface RpcAsyncProxy {
    RpcFuture call(String methodName, Object[] params);
}
