package com.tristeza.client;

import com.google.protobuf.GeneratedMessageV3;
import com.tristeza.builder.MessageBuilder;
import com.tristeza.handler.NettyClientHandler;
import com.tristeza.protobuf.MessageModule;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chaodong.xi
 * @date 2021/1/27 下午4:19
 */

public class NettyClient {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private final AtomicBoolean isConnect = new AtomicBoolean(false);
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;

    private Channel channel;

    private NettyClient() {
    }

    private static class SingletonHolder {
        private static final NettyClient INSTANCE = new NettyClient();
    }

    public static NettyClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void init() {
        if (!isConnect.get()) {
            try {
                this.connect(HOST, PORT);
                isConnect.set(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                        protected void initChannel(SocketChannel sc) {
                            sc.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            sc.pipeline().addLast(new ProtobufDecoder(MessageModule.Message.getDefaultInstance()));
                            sc.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            sc.pipeline().addLast(new ProtobufEncoder());
                            sc.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            this.channel = channelFuture.channel();
            channel.closeFuture().sync();
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

    public void sendMessage(String module, String cmd, GeneratedMessageV3 data) {
        this.channel.writeAndFlush(MessageBuilder.getRequestMessage(module, cmd, data));
    }
}
