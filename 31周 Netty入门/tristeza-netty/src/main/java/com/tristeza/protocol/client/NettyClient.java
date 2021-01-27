package com.tristeza.protocol.client;

import com.tristeza.enums.NettyConstant;
import com.tristeza.protocol.handler.NettyMessageDecoder;
import com.tristeza.protocol.handler.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午8:26
 */
public class NettyClient {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                            sc.pipeline().addLast(new NettyMessageEncoder());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCAL_IP, NettyConstant.LOCAL_PORT)).sync();

            System.out.println("Client Start..");

            channelFuture.channel().closeFuture().sync();
            group.shutdownGracefully();
        } finally {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
    }
}
