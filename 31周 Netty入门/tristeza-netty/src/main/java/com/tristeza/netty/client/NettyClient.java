package com.tristeza.netty.client;

import com.tristeza.common.utils.MarshallingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午11:21
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup wGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(wGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) {
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());
                        sc.pipeline().addLast(new NettyClientHandler());
                    }
                });

        ChannelFuture cf = bootstrap.connect("127.0.0.1", 8766).sync();
        cf.channel().closeFuture().sync();
        wGroup.shutdownGracefully();
    }
}
