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
        RpcRequest request = buildSyncRpcRequest(method, args);
        RpcFuture rpcFuture = handler.sendRequest(request);

        return rpcFuture.get(timeout, TimeUnit.SECONDS);
    }

    private RpcRequest buildSyncRpcRequest(Method method, Object[] args) {
        RpcRequest request = new RpcRequest();

        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        return request;
    }

    private RpcRequest buildAsyncRpcRequest(String methodName, Object... params) {
        RpcRequest request = new RpcRequest();

        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(clazz.getName());
        request.setMethodName(methodName);
        request.setParameterTypes(buildParamsTypesByParam(params));
        request.setParameters(params);

        return request;
    }

    private Class<?>[] buildParamsTypesByParam(Object[] params) {
        Class<?>[] paramsType = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            paramsType[i] = getClassType(params[i]);
        }

        return paramsType;
    }

    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName) {
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }
        return classType;
    }

    @Override
    public RpcFuture call(String methodName, Object... params) {
        RpcClientHandler handler = RpcConnectManager.getInstance().chooseHandler();
        RpcRequest request = buildAsyncRpcRequest(methodName, params);

        return handler.sendRequest(request);
    }
}
