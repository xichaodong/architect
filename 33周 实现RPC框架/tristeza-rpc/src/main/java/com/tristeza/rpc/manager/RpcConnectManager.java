package com.tristeza.rpc.manager;

import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.initializer.RpcClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午8:38
 */
public class RpcConnectManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcConnectManager.class);
    private static final RpcConnectManager RPC_CONNECT_MANAGER = new RpcConnectManager();

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));
    private final Map<SocketAddress, RpcClientHandler> connectedHandlerMap = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<RpcClientHandler> connectedHandlerList = new CopyOnWriteArrayList<>();
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private final ReentrantLock connectedLock = new ReentrantLock();
    private final Condition connectedCondition = connectedLock.newCondition();

    private static final long CONNECT_TIMEOUT_MILLS = 6000;
    private volatile boolean isRunning = true;
    private final AtomicInteger handlerIndex = new AtomicInteger(0);

    private RpcConnectManager() {
    }

    public static RpcConnectManager getInstance() {
        return RPC_CONNECT_MANAGER;
    }

    public void connect(String serverAddress) {
        List<String> allServerAddress = Arrays.asList(serverAddress.split(","));
        updateConnectedServer(allServerAddress);
    }

    public void updateConnectedServer(List<String> allServerAddress) {
        if (CollectionUtils.isEmpty(allServerAddress)) {
            LOGGER.error("no available server address");
            return;
        }

        Set<SocketAddress> newAllServerNodeSet = new HashSet<>();

        for (String serverAddress : allServerAddress) {
            String[] array = serverAddress.split(":");
            if (array.length == 2) {
                newAllServerNodeSet.add(new InetSocketAddress(array[0], Integer.parseInt(array[1])));
            }
        }

        for (SocketAddress address : newAllServerNodeSet) {
            if (!connectedHandlerMap.containsKey(address)) {
                connectAsync(address);
            }
        }

        for (int i = 0; i < connectedHandlerList.size(); i++) {
            RpcClientHandler handler = connectedHandlerList.get(i);
            SocketAddress remotePeer = handler.getRemotePeer();
            if (!newAllServerNodeSet.contains(remotePeer)) {
                LOGGER.info("移除遗留节点缓存，" + remotePeer);
                RpcClientHandler rpcClientHandler = connectedHandlerMap.get(remotePeer);
                if (Objects.nonNull(rpcClientHandler)) {
                    rpcClientHandler.close();
                    connectedHandlerMap.remove(remotePeer);
                }
                connectedHandlerList.remove(handler);
            }
        }

    }


    private void connectAsync(SocketAddress address) {
        threadPoolExecutor.submit(() -> {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler())
                    .handler(new RpcClientInitializer());

            connect(bootstrap, address);
        });
    }

    private void connect(Bootstrap bootstrap, SocketAddress remotePeer) {
        ChannelFuture channelFuture = bootstrap.connect(remotePeer);
        channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
            LOGGER.error("连接远程服务器失败，地址 = {}", remotePeer);
            future.channel().eventLoop().schedule(() -> {
                clearConnected();
                connect(bootstrap, remotePeer);
            }, 3, TimeUnit.SECONDS);
        });
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                LOGGER.info("连接远程服务器成功，地址 = {}", remotePeer);
                addHandler(future.channel().pipeline().get(RpcClientHandler.class));
            }
        });
    }

    private void addHandler(RpcClientHandler handler) {
        connectedHandlerMap.put(handler.getChannel().remoteAddress(), handler);
        connectedHandlerList.add(handler);
        signalAvailableHandler();
    }

    private boolean waitingForAvailableHandler() throws InterruptedException {
        try {
            connectedLock.lock();
            return connectedCondition.await(CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS);
        } finally {
            connectedLock.unlock();
        }
    }

    private void signalAvailableHandler() {
        connectedLock.lock();
        try {
            connectedCondition.signalAll();
        } finally {
            connectedLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public RpcClientHandler chooseHandler() {
        CopyOnWriteArrayList<RpcClientHandler> handlers = (CopyOnWriteArrayList<RpcClientHandler>) (this.connectedHandlerList.clone());
        int size = handlers.size();
        while (isRunning && handlers.size() <= 0) {
            try {
                boolean available = waitingForAvailableHandler();
                if (available) {
                    handlers = (CopyOnWriteArrayList<RpcClientHandler>) (this.connectedHandlerList.clone());
                    size = handlers.size();
                }
            } catch (Exception e) {
                LOGGER.error("等待可用连接过程中被打断");
                throw new RuntimeException("没有可用连接");
            }
        }

        if (!isRunning) {
            return null;
        }

        return handlers.get(handlerIndex.getAndIncrement() % size);
    }

    public void stop() {
        isRunning = false;
        connectedHandlerList.forEach(RpcClientHandler::close);

        signalAvailableHandler();

        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void reconnect(RpcClientHandler handler, SocketAddress remotePeer) {
        if (Objects.nonNull(handler)) {
            handler.close();
            connectedHandlerList.remove(handler);
            connectedHandlerMap.remove(remotePeer);
        }
        connectAsync(remotePeer);
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
}
