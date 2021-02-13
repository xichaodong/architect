package com.tristeza.rpc.main.provider;

import com.google.common.collect.ImmutableList;
import com.tristeza.rpc.config.node.impl.ProviderConfig;
import com.tristeza.rpc.config.starter.RpcServerConfig;

/**
 * @author chaodong.xi
 * @date 2021/2/12 10:20 下午
 */
public class ProviderStarter {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ProviderConfig providerConfig = new ProviderConfig();
                providerConfig.setInterfaceClass("com.tristeza.rpc.main.consumer.HelloService");
                providerConfig.setRef(new HelloServiceImpl());

                RpcServerConfig rpcServerConfig = new RpcServerConfig(ImmutableList.of(providerConfig));
                rpcServerConfig.setPort(8765);
                rpcServerConfig.exporter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
