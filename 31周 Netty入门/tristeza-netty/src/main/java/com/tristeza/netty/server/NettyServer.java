package com.tristeza.netty.server;

import com.tristeza.common.utils.MarshallingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午10:22
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bGroup = new NioEventLoopGroup(1);
        EventLoopGroup wGroup = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();


        sb.group(bGroup, wGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) {
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });

        ChannelFuture cf = sb.bind(8766).sync();

        cf.channel().closeFuture().sync();

        bGroup.shutdownGracefully();
        wGroup.shutdownGracefully();
    }
}
