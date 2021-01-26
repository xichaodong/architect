package com.tristeza.netty.server;

import com.tristeza.common.utils.MarshallingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.marshalling.MarshallingDecoder;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午10:22
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bGroup = new DefaultEventLoopGroup(1);
        EventLoopGroup wGroup = new DefaultEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();


        sb.group(bGroup, wGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) {
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });

        ChannelFuture cf = sb.bind(8765).sync();

        cf.channel().closeFuture().sync();

        bGroup.shutdownGracefully();
        wGroup.shutdownGracefully();
    }
}
