package com.tristeza.rpc.listener;

import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.task.ReconnectionTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chaodong.xi
 * @date 2021/1/30 下午3:16
 */
public class ChannelConnectFailedListener implements ChannelFutureListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(ChannelConnectFailedListener.class);

    private final Map<SocketAddress, RpcClientHandler> connectedHandlerMap;
    private final List<RpcClientHandler> connectedHandlerList;
    private final Bootstrap bootstrap;
    private final SocketAddress remotePeer;
    private final ReentrantLock connectedLock;
    private final Condition connectedCondition;

    public ChannelConnectFailedListener(Map<SocketAddress, RpcClientHandler> connectedHandlerMap, List<RpcClientHandler> connectedHandlerList, Bootstrap bootstrap,
                                        SocketAddress remotePeer, ReentrantLock connectedLock, Condition connectedCondition) {
        this.connectedHandlerMap = connectedHandlerMap;
        this.connectedHandlerList = connectedHandlerList;
        this.bootstrap = bootstrap;
        this.remotePeer = remotePeer;
        this.connectedLock = connectedLock;
        this.connectedCondition = connectedCondition;
    }

    @Override
    public void operationComplete(ChannelFuture future) {
        LOGGER.error("error ch");
        future.channel().eventLoop().schedule(new ReconnectionTask(connectedHandlerMap, connectedHandlerList,
                bootstrap, remotePeer, connectedLock, connectedCondition), 3, TimeUnit.SECONDS);
    }
}
