package com.tristeza.rpc.task;

import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.initializer.RpcClientInitializer;
import com.tristeza.rpc.listener.ChannelConnectFailedListener;
import com.tristeza.rpc.listener.ChannelConnectSuccessListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chaodong.xi
 * @date 2021/1/30 下午2:59
 */
public class AsyncConnectTask implements Runnable {
    private final Map<SocketAddress, RpcClientHandler> connectedHandlerMap;
    private final List<RpcClientHandler> connectedHandlerList;
    private final EventLoopGroup eventLoopGroup;
    private final SocketAddress remotePeer;
    private final ReentrantLock connectedLock;
    private final Condition connectedCondition;

    public AsyncConnectTask(Map<SocketAddress, RpcClientHandler> connectedHandlerMap, List<RpcClientHandler> connectedHandlerList,
                            EventLoopGroup eventLoopGroup, SocketAddress remotePeer, ReentrantLock connectedLock, Condition connectedCondition) {
        this.connectedHandlerMap = connectedHandlerMap;
        this.connectedHandlerList = connectedHandlerList;
        this.eventLoopGroup = eventLoopGroup;
        this.remotePeer = remotePeer;
        this.connectedLock = connectedLock;
        this.connectedCondition = connectedCondition;
    }

    @Override
    public void run() {
        Bootstrap bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler())
                .handler(new RpcClientInitializer());

        connect(bootstrap);
    }

    private void connect(Bootstrap bootstrap) {
        ChannelFuture channelFuture = bootstrap.connect(remotePeer);
        channelFuture.channel().closeFuture().addListener(new ChannelConnectFailedListener(connectedHandlerMap,
                connectedHandlerList, bootstrap, remotePeer, connectedLock, connectedCondition));
        channelFuture.addListener(new ChannelConnectSuccessListener(connectedHandlerMap, connectedHandlerList, remotePeer,
                connectedLock, connectedCondition));
    }
}
