package com.tristeza.rpc.listener;

import com.tristeza.rpc.handler.RpcClientHandler;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelFutureListener;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chaodong.xi
 * @date 2021/1/30 下午3:16
 */
public class ChannelConnectSuccessListener implements ChannelFutureListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(ChannelConnectSuccessListener.class);

    private final Map<SocketAddress, RpcClientHandler> connectedHandlerMap;
    private final List<RpcClientHandler> connectedHandlerList;
    private final SocketAddress remoteBear;
    private final ReentrantLock connectedLock;
    private final Condition connectedCondition;

    public ChannelConnectSuccessListener(Map<SocketAddress, RpcClientHandler> connectedHandlerMap, List<RpcClientHandler> connectedHandlerList,
                                         SocketAddress remoteBear, ReentrantLock connectedLock, Condition connectedCondition) {
        this.connectedHandlerMap = connectedHandlerMap;
        this.connectedHandlerList = connectedHandlerList;
        this.remoteBear = remoteBear;
        this.connectedLock = connectedLock;
        this.connectedCondition = connectedCondition;
    }

    @Override
    public void operationComplete(ChannelFuture future) {
        if (future.isSuccess()) {
            LOGGER.info("连接远程服务器成功，地址 = {}", remoteBear);
            addHandler(future.channel().pipeline().get(RpcClientHandler.class));
        }
    }

    private void addHandler(RpcClientHandler handler) {
        connectedHandlerMap.put(handler.getRemotePeer(), handler);
        connectedHandlerList.add(handler);
    }

    private void signalAvailableHandler() {
        connectedLock.lock();
        try {
            connectedCondition.signalAll();
        } finally {
            connectedLock.unlock();
        }
    }
}
