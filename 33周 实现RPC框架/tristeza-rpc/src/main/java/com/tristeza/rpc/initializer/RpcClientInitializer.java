package com.tristeza.rpc.initializer;

import com.tristeza.rpc.codec.RpcDecoder;
import com.tristeza.rpc.codec.RpcEncoder;
import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.model.RpcRequest;
import com.tristeza.rpc.model.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author chaodong.xi
 * @date 2021/1/30 下午3:03
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
        ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
        ch.pipeline().addLast(new RpcClientHandler());
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 0));
    }
}
