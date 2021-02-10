package com.tristeza.rpc.proxy.impl;

import com.tristeza.rpc.async.RpcFuture;
import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.manager.RpcConnectManager;
import com.tristeza.rpc.model.RpcRequest;
import com.tristeza.rpc.proxy.RpcAsyncProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/2/10 11:53 下午
 */
public class RpcProxyImpl<T> implements InvocationHandler, RpcAsyncProxy {
    private final Class<T> clazz;
    private final long timeout;

    public RpcProxyImpl(Class<T> clazz, long timeout) {
        this.clazz = clazz;
        this.timeout = timeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcClientHandler handler = RpcConnectManager.getInstance().chooseHandler();
        RpcRequest request = buildSyncRpcRequest(method);
        RpcFuture rpcFuture = handler.sendRequest(request);

        return rpcFuture.get(timeout, TimeUnit.SECONDS);
    }

    private RpcRequest buildSyncRpcRequest(Method method) {
        RpcRequest request = new RpcRequest();

        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(method.getParameters());

        return request;
    }

    private RpcRequest buildAsyncRpcRequest(String methodName, Object[] params) {
        RpcRequest request = new RpcRequest();

        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(methodName);
        request.setParameterTypes(buildParamsTypesByParam(params));
        request.setParameters(params);

        return request;
    }

    private Class<?>[] buildParamsTypesByParam(Object[] params) {
        Class<?>[] paramsType = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            paramsType[i] = params.getClass();
        }

        return paramsType;
    }

    @Override
    public RpcFuture call(String methodName, Object[] params) {
        RpcClientHandler handler = RpcConnectManager.getInstance().chooseHandler();
        RpcRequest request = buildAsyncRpcRequest(methodName, params);

        return handler.sendRequest(request);
    }
}
