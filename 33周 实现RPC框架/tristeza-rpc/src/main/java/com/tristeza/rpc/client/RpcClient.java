package com.tristeza.rpc.client;

import com.tristeza.rpc.manager.RpcConnectManager;
import com.tristeza.rpc.proxy.RpcAsyncProxy;
import com.tristeza.rpc.proxy.impl.RpcProxyImpl;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chaodong.xi
 * @date 2021/2/9 5:32 下午
 */
public class RpcClient {
    private final Map<Class<?>, Object> syncProxyInstanceMap = new HashMap<>();
    private final Map<Class<?>, RpcAsyncProxy> asyncProxyInstanceMap = new HashMap<>();
    private String serverAddress;
    private long timeout;

    public void initRpcClient(String serverAddress, long timeout) {
        this.serverAddress = serverAddress;
        this.timeout = timeout;
        connect();
    }

    private void connect() {
        RpcConnectManager.getInstance().connect(serverAddress);
    }

    private void stop() {
        RpcConnectManager.getInstance().stop();
    }

    @SuppressWarnings("unchecked")
    public <T> T invokeSync(Class<T> interfaceClass) {
        if (syncProxyInstanceMap.containsKey(interfaceClass)) {
            return (T) syncProxyInstanceMap.get(interfaceClass);
        }

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                new RpcProxyImpl<>(interfaceClass, timeout));

        syncProxyInstanceMap.put(interfaceClass, proxyInstance);

        return (T) proxyInstance;
    }

    public <T> RpcAsyncProxy invokeAsync(Class<T> interfaceClass) {
        if (asyncProxyInstanceMap.containsKey(interfaceClass)) {
            return asyncProxyInstanceMap.get(interfaceClass);
        } else {
            RpcAsyncProxy asyncProxy = new RpcProxyImpl<>(interfaceClass, timeout);
            asyncProxyInstanceMap.put(interfaceClass, asyncProxy);
            return asyncProxy;
        }
    }
}
