package com.tristeza.rpc.client;

import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.task.AsyncConnectTask;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.*;
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
    private final List<RpcClientHandler> connectedHandlerList = new CopyOnWriteArrayList<>();
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private final ReentrantLock connectedLock = new ReentrantLock();
    private final Condition connectedCondition = connectedLock.newCondition();

    private RpcConnectManager() {
    }

    public static RpcConnectManager getInstance() {
        return RPC_CONNECT_MANAGER;
    }

    public void connect(String serverAddress) {
        List<String> allServerAddress = Arrays.asList(serverAddress.split(","));

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

    }


    private void connectAsync(SocketAddress address) {
        threadPoolExecutor.submit(new AsyncConnectTask(connectedHandlerMap, connectedHandlerList, eventLoopGroup,
                address, connectedLock, connectedCondition));
    }
}
