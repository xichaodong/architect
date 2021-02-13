package com.tristeza.rpc.config.starter;

import com.tristeza.rpc.config.node.impl.ProviderConfig;
import com.tristeza.rpc.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2021/2/11 12:54 上午
 */
public class RpcServerConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcServerConfig.class);

    private static final String HOST = "127.0.0.1";
    protected int port;
    private final List<ProviderConfig> providerConfigs;
    private RpcServer rpcServer;

    public RpcServerConfig(List<ProviderConfig> providerConfigs) {
        this.providerConfigs = providerConfigs;
    }

    public void exporter() {
        if (Objects.isNull(rpcServer)) {
            try {
                rpcServer = new RpcServer(String.format("%s:%s", HOST, port));
            } catch (Exception e) {
                e.printStackTrace();
            }

            providerConfigs.forEach(providerConfig -> rpcServer.registerProcessor(providerConfig));
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
