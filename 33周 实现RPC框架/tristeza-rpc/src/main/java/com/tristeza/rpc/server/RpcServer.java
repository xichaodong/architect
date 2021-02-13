package com.tristeza.rpc.server;

import com.tristeza.rpc.config.node.impl.ProviderConfig;
import com.tristeza.rpc.handler.RpcServerHandler;
import com.tristeza.rpc.initializer.RpcClientInitializer;
import com.tristeza.rpc.initializer.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/2/9 10:32 下午
 */
public class RpcServer {
    public static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private final String serverAddress;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private final Map<String, Object> handlerMap = new HashMap<>();

    public RpcServer(String serverAddress) throws InterruptedException {
        this.serverAddress = serverAddress;
        start();
    }

    private void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new LoggingHandler())
                .childHandler(new RpcServerInitializer(handlerMap));

        String[] address = serverAddress.split(":");

        ChannelFuture channelFuture = bootstrap.bind(address[0], Integer.parseInt(address[1])).sync();

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                LOGGER.info("连接绑定成功，地址 = {}", serverAddress);
            } else {
                LOGGER.error("连接绑定失败，地址 = {}", serverAddress);
                throw new Exception("连接绑定失败，原因 = ", future.cause());
            }
        });

        try {
            channelFuture.await(5000, TimeUnit.MILLISECONDS);
            if (channelFuture.isSuccess()) {
                LOGGER.info("连接绑定成功，地址 = {}", serverAddress);
            }
        } catch (Exception e) {
            LOGGER.error("建立连接被中断，原因 = ", e);
        }
    }

    public void registerProcessor(ProviderConfig providerConfig) {
        handlerMap.put(providerConfig.getInterfaceClass(), providerConfig.getRef());
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
