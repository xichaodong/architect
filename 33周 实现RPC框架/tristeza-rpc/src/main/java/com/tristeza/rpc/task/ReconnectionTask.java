package com.tristeza.rpc.task;

import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.listener.ChannelConnectFailedListener;
import com.tristeza.rpc.listener.ChannelConnectSuccessListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author chaodong.xi
 * @date 2021/1/30 下午3:25
 */
public class ReconnectionTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectionTask.class);

    private final Map<SocketAddress, RpcClientHandler> connectedHandlerMap;
    private final List<RpcClientHandler> connectedHandlerList;
    private final Bootstrap bootstrap;
    private final SocketAddress remotePeer;
    private final ReentrantLock connectedLock;
    private final Condition connectedCondition;

    public ReconnectionTask(Map<SocketAddress, RpcClientHandler> connectedHandlerMap, List<RpcClientHandler> connectedHandlerList,
                            Bootstrap bootstrap, SocketAddress remotePeer, ReentrantLock connectedLock, Condition connectedCondition) {
        this.connectedHandlerMap = connectedHandlerMap;
        this.connectedHandlerList = connectedHandlerList;
        this.bootstrap = bootstrap;
        this.remotePeer = remotePeer;
        this.connectedLock = connectedLock;
        this.connectedCondition = connectedCondition;
    }

    @Override
    public void run() {
        LOGGER.error("连接远程服务器失败，开始重连");
        clearConnected();
        connect();
    }

    private void clearConnected() {
        for (RpcClientHandler clientHandler : connectedHandlerList) {
            SocketAddress remotePeer = clientHandler.getRemotePeer();
            RpcClientHandler handler = connectedHandlerMap.get(remotePeer);

            if (Objects.nonNull(handler)) {
                handler.close();
                connectedHandlerMap.remove(remotePeer);
            }
        }
    }

    private void connect() {
        ChannelFuture channelFuture = bootstrap.connect(remotePeer);
        channelFuture.channel().closeFuture().addListener(new ChannelConnectFailedListener(connectedHandlerMap, connectedHandlerList,
                bootstrap, remotePeer, connectedLock, connectedCondition));
        channelFuture.addListener(new ChannelConnectSuccessListener(connectedHandlerMap, connectedHandlerList,
                remotePeer, connectedLock, connectedCondition));
    }
}
