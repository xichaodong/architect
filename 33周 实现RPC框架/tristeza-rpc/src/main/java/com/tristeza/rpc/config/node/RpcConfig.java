package com.tristeza.rpc.config.node;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chaodong.xi
 * @date 2021/2/9 11:47 下午
 */
public abstract class RpcConfig {
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final String ID_PREFIX = "RPC_ID_PREFIX";

    protected String id;
    protected String interfaceClass;
    protected Class<?> proxyClass;

    public String getId() {
        if (StringUtils.isBlank(id)) {
            id = String.format("%s_%d", ID_PREFIX, idGenerator.getAndIncrement());
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(String interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<?> getProxyClass() {
        return proxyClass;
    }

    public void setProxyClass(Class<?> proxyClass) {
        this.proxyClass = proxyClass;
    }
}
