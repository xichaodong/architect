package com.tristeza.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/1/27 下午4:19
 */
@Component
public class NettyClient {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;

    private Channel channel;

    public NettyClient() throws InterruptedException {
        connect(HOST, PORT);
    }

    public void connect(String host, int port) throws InterruptedException {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    try {
                        connect(host, port);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            });
        }
    }
}
