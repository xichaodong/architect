package com.tristeza.rpc.config.node.impl;

import com.tristeza.rpc.config.node.RpcConfig;

/**
 * @author chaodong.xi
 * @date 2021/2/9 11:00 下午
 */
public class ProviderConfig extends RpcConfig {
    protected Object ref;

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }
}
