package com.tristeza.netty.client;

import com.tristeza.common.data.Request;
import com.tristeza.common.utils.MarshallingUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Scanner;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午11:21
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup wGroup = new DefaultEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(wGroup)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel sc) {
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());
                        sc.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());
                        sc.pipeline().addLast(new NettyClientHandler());
                    }
                });

        ChannelFuture cf = bootstrap.connect("127.0.0.1", 8765).sync();
        for (int i = 0; i < 100; i++) {
            Request request = new Request();
            request.setId(String.valueOf(i));
            request.setName("msg:" + i);
            request.setRequestMessage("内容:" + i);
        }
    }
}
