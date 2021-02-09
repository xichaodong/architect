package com.tristeza.rpc.client;

import com.tristeza.rpc.manager.RpcConnectManager;

/**
 * @author chaodong.xi
 * @date 2021/2/9 5:32 下午
 */
public class RpcClient {
    private String serverAddress;

    private long timeout;

    private RpcClient() {

    }

    public void initRpcClient(String serverAddress, long timeout) {
        this.serverAddress = serverAddress;
        this.timeout = timeout;
    }

    private void connect() {
        RpcConnectManager.getInstance().connect(serverAddress);
    }

    private void stop() {
        RpcConnectManager.getInstance().stop();
    }
}
